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

import com.matrimony.model.dto.request.GothraRequest;
import com.matrimony.model.dto.response.GothraResponse;
import com.matrimony.model.entity.Gothra;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.GothraRepository;
import com.matrimony.service.GothraService;

@Service
public class GothraServiceImpl implements GothraService {

	@Autowired
	private GothraRepository gothraRepository;

	private GothraResponse mapToResponse(Gothra entity) {
		GothraResponse response = new GothraResponse();
		response.setId(entity.getId());
		response.setGothraName(entity.getGothraName());
		return response;
	}

	private void updateFromRequest(Gothra entity, GothraRequest request) {
		if (request.getGothraName() != null) {
			entity.setGothraName(request.getGothraName());
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateGothra(GothraRequest request) {
		try {
			Gothra existing = gothraRepository.findByGothraName(request.getGothraName());
			if (existing != null) {
				existing.setGothraName(request.getGothraName());
				gothraRepository.save(existing);
				return new ResponseEntity("Gothra updated successfully", HttpStatus.OK.value(),
						mapToResponse(existing));
			} else {
				Gothra newEntity = new Gothra();
				updateFromRequest(newEntity, request);
				gothraRepository.save(newEntity);
				return new ResponseEntity("Gothra created successfully", HttpStatus.CREATED.value(),
						mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Gothra: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getGothraById(Long id) {
		try {
			Gothra entity = gothraRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Gothra not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Gothra found", 200, mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Gothra: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllGothras() {
		try {
			List<Gothra> list = gothraRepository.findAll();

			if (list.isEmpty()) {
				return new ResponseEntity("No Gothras found", HttpStatus.NOT_FOUND.value(), null);
			}

			List<GothraResponse> responseList = list.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Gothras fetched successfully", HttpStatus.OK, responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Gothras: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteGothra(Long id) {
		try {
			Gothra entity = gothraRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Gothra not found", HttpStatus.NOT_FOUND.value(), null);
			}
			gothraRepository.delete(entity);
			return new ResponseEntity("Gothra deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Gothra: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadGothraExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			List<Gothra> list = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);
				if (nameCell == null || nameCell.getCellType() != CellType.STRING)
					continue;

				String name = nameCell.getStringCellValue().trim();
				if (name.isEmpty())
					continue;

				Gothra entity = gothraRepository.findByGothraName(name);
				if (entity == null) {
					entity = new Gothra();
					entity.setGothraName(name);
					list.add(entity);
				}
			}

			if (!list.isEmpty()) {
				gothraRepository.saveAll(list);
			}

			return new ResponseEntity("Gothra data uploaded successfully", HttpStatus.OK.value(),
					list.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}
