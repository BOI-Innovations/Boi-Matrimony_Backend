package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.AnnualIncomeRequest;
import com.matrimony.model.dto.response.AnnualIncomeResponse;
import com.matrimony.model.entity.AnnualIncomeEntity;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.AnnualIncomeRepository;
import com.matrimony.service.AnnualIncomeService;

@Service
public class AnnualIncomeServiceImpl implements AnnualIncomeService {

	@Autowired
	private AnnualIncomeRepository repository;

	private AnnualIncomeResponse mapToResponse(AnnualIncomeEntity entity) {
		AnnualIncomeResponse response = new AnnualIncomeResponse();
		response.setId(entity.getId());
		response.setIncomeRange(entity.getIncomeRange());
		return response;
	}

	private void updateFromRequest(AnnualIncomeEntity entity, AnnualIncomeRequest request) {
		if (request.getIncomeRange() != null) {
			entity.setIncomeRange(request.getIncomeRange());
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateAnnualIncome(AnnualIncomeRequest request) {
		try {
			AnnualIncomeEntity existing = repository.findAll().stream()
					.filter(i -> i.getIncomeRange().equalsIgnoreCase(request.getIncomeRange())).findFirst()
					.orElse(null);

			if (existing != null) {
				existing.setIncomeRange(request.getIncomeRange());
				repository.save(existing);
				return new ResponseEntity("Annual Income updated successfully", HttpStatus.OK.value(),
						mapToResponse(existing));
			} else {
				AnnualIncomeEntity newEntity = new AnnualIncomeEntity();
				updateFromRequest(newEntity, request);
				repository.save(newEntity);
				return new ResponseEntity("Annual Income created successfully", HttpStatus.CREATED.value(),
						mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Annual Income: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAnnualIncomeById(Long id) {
		try {
			AnnualIncomeEntity entity = repository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Annual Income not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Annual Income found", HttpStatus.OK.value(), mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Annual Income: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllAnnualIncomes() {
		try {
			List<AnnualIncomeEntity> list = repository.findAll();
			if (list.isEmpty()) {
				return new ResponseEntity("No Annual Income records found", HttpStatus.NOT_FOUND.value(), null);
			}

			List<AnnualIncomeResponse> responseList = list.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Annual Income records fetched successfully", HttpStatus.OK.value(),
					responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Annual Income records: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteAnnualIncome(Long id) {
		try {
			AnnualIncomeEntity entity = repository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Annual Income not found", HttpStatus.NOT_FOUND.value(), null);
			}
			repository.delete(entity);
			return new ResponseEntity("Annual Income deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Annual Income: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadAnnualIncomeExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			List<AnnualIncomeEntity> newRecords = new ArrayList<>();

			// ✅ Start from 1 to skip header row
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				// ✅ Column 1 → Annual Income (skip Id column)
				Cell incomeCell = row.getCell(1);
				if (incomeCell == null)
					continue;

				incomeCell.setCellType(CellType.STRING);
				String incomeRange = incomeCell.getStringCellValue().trim();

				if (incomeRange.isEmpty())
					continue;

				// ✅ Check if already exists (case-insensitive)
				boolean exists = repository.findAll().stream()
						.anyMatch(a -> a.getIncomeRange().equalsIgnoreCase(incomeRange));

				if (!exists) {
					AnnualIncomeEntity entity = new AnnualIncomeEntity();
					entity.setIncomeRange(incomeRange);
					newRecords.add(entity);
				}
			}

			if (!newRecords.isEmpty()) {
				repository.saveAll(newRecords);
			}

			return new ResponseEntity("Annual Income data uploaded successfully", HttpStatus.OK.value(),
					newRecords.size() + " new records processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}
