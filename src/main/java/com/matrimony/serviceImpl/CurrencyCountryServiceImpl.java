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

import com.matrimony.model.dto.request.CurrencyCountryRequest;
import com.matrimony.model.dto.response.CurrencyCountryResponse;
import com.matrimony.model.entity.CurrencyCountry;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.CurrencyCountryRepository;
import com.matrimony.service.CurrencyCountryService;

@Service
public class CurrencyCountryServiceImpl implements CurrencyCountryService {

	@Autowired
	private CurrencyCountryRepository repository;

	private CurrencyCountryResponse mapToResponse(CurrencyCountry entity) {
		CurrencyCountryResponse response = new CurrencyCountryResponse();
		response.setId(entity.getId());
		response.setCurrencyCountryName(entity.getCurrencyCountryName());
		return response;
	}

	private void updateFromRequest(CurrencyCountry entity, CurrencyCountryRequest request) {
		if (request.getCurrencyCountryName() != null) {
			entity.setCurrencyCountryName(request.getCurrencyCountryName());
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateCurrencyCountry(CurrencyCountryRequest request) {
		try {
			CurrencyCountry existing = repository.findByCurrencyCountryName(request.getCurrencyCountryName());
			if (existing != null) {
				existing.setCurrencyCountryName(request.getCurrencyCountryName());
				repository.save(existing);
				return new ResponseEntity("Currency-Country updated successfully", HttpStatus.OK.value(),
						mapToResponse(existing));
			} else {
				CurrencyCountry newEntity = new CurrencyCountry();
				updateFromRequest(newEntity, request);
				repository.save(newEntity);
				return new ResponseEntity("Currency-Country created successfully", HttpStatus.CREATED.value(),
						mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Currency-Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getCurrencyCountryById(Long id) {
		try {
			CurrencyCountry entity = repository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Currency-Country not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Currency-Country found", 200, mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Currency-Country: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllCurrencyCountries() {
		try {
			List<CurrencyCountry> list = repository.findAll();
			if (list.isEmpty()) {
				return new ResponseEntity("No Currency-Country records found", HttpStatus.NOT_FOUND.value(), null);
			}

			List<CurrencyCountryResponse> responseList = list.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Currency-Country records fetched successfully", HttpStatus.OK, responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching records: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteCurrencyCountry(Long id) {
		try {
			CurrencyCountry entity = repository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Currency-Country not found", HttpStatus.NOT_FOUND.value(), null);
			}
			repository.delete(entity);
			return new ResponseEntity("Currency-Country deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Currency-Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadCurrencyCountryExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			List<CurrencyCountry> list = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);
				if (nameCell == null || nameCell.getCellType() != CellType.STRING)
					continue;

				String name = nameCell.getStringCellValue().trim(); // e.g. "India-Rs."
				if (name.isEmpty())
					continue;

				CurrencyCountry entity = repository.findByCurrencyCountryName(name);
				if (entity == null) {
					entity = new CurrencyCountry();
					entity.setCurrencyCountryName(name);
					list.add(entity);
				}
			}

			if (!list.isEmpty()) {
				repository.saveAll(list);
			}

			return new ResponseEntity("Currency-Country data uploaded successfully", HttpStatus.OK.value(),
					list.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}
}
