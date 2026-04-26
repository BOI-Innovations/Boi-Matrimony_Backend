package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.matrimony.model.dto.request.HeightRequest;
import com.matrimony.model.dto.response.HeightResponse;
import com.matrimony.model.entity.Height;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.HeightRepository;
import com.matrimony.service.HeightService;

@Service
public class HeightServiceImpl implements HeightService {

	@Autowired
	private HeightRepository heightRepository;

	private HeightResponse mapToResponse(Height entity) {
		HeightResponse response = new HeightResponse();
		response.setId(entity.getId());
		response.setHeight(entity.getHeight());
		return response;
	}

	private void updateFromRequest(Height entity, HeightRequest request) {
		if (request.getHeight() != null) {
			entity.setHeight(request.getHeight());
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateHeight(HeightRequest request) {
		try {
			Height existing = heightRepository.findByHeight(request.getHeight());
			if (existing != null) {
				existing.setHeight(request.getHeight());
				heightRepository.save(existing);
				return new ResponseEntity("Height updated successfully", 200, mapToResponse(existing));
			} else {
				Height newEntity = new Height();
				updateFromRequest(newEntity, request);
				heightRepository.save(newEntity);
				return new ResponseEntity("Height created successfully", 201, mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Height: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getHeightById(Long id) {
		try {
			Height entity = heightRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Height not found", 404, null);
			}
			return new ResponseEntity("Height found", 200, mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Height: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllHeights() {
		try {
			List<Height> list = heightRepository.findAll();
			List<HeightResponse> responseList = list.stream()
				    .map(this::mapToResponse)
				    .sorted(Comparator.comparingInt(h -> convertToInches(h.getHeight())))
				    .toList();
			return new ResponseEntity("All Heights fetched successfully", 200, responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Heights: " + e.getMessage(), 500, null);
		}
	}
	
	private int convertToInches(String height) {
	    int feet = 0;
	    int inches = 0;

	    String[] parts = height.split(" ");

	    if (parts.length >= 2) {
	        feet = Integer.parseInt(parts[0]);
	    }

	    if (parts.length >= 4) {
	        inches = Integer.parseInt(parts[2]);
	    }

	    return feet * 12 + inches;
	}

	@Override
	@Transactional
	public ResponseEntity deleteHeight(Long id) {
		try {
			Height entity = heightRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Height not found", 404, null);
			}
			heightRepository.delete(entity);
			return new ResponseEntity("Height deleted successfully", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Height: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadHeightExcel(MultipartFile file) {
	    if (file == null || file.isEmpty()) {
	        return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
	    }

	    int rowCount = 0;

	    try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        if (sheet == null) {
	            return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
	        }

	        List<Height> list = new ArrayList<>();

	        // Start from row 1 (skip header row)
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            // Skip first column (ID) → read height from column 1
	            Cell nameCell = row.getCell(1);
	            if (nameCell == null || nameCell.getCellType() != CellType.STRING) continue;

	            String heightValue = nameCell.getStringCellValue().trim();
	            if (heightValue.isEmpty()) continue;

	            // Validate against regex from entity
	            if (!heightValue.matches("^\\d+\\sfeet(\\s\\d+\\s(inches|inch))?$")) {
	                continue; // skip invalid format
	            }

	            Height entity = new Height(); // always new record (ID auto-generates)
	            entity.setHeight(heightValue);
	            list.add(entity);
	            rowCount++;
	        }

	        if (!list.isEmpty()) {
	            heightRepository.saveAll(list);
	        }

	        return new ResponseEntity("Height data uploaded successfully", HttpStatus.OK.value(),
	                rowCount + " rows inserted");

	    } catch (Exception e) {
	        return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
	                HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
	    }
	}


}
