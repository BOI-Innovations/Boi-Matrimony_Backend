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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.StarRequest;
import com.matrimony.model.dto.response.StarResponse;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.Star;
import com.matrimony.repository.StarRepository;
import com.matrimony.service.StarService;

@Service
public class StarServiceImpl implements StarService {

	@Autowired
	private StarRepository starRepository;

	private StarResponse mapToResponse(Star entity) {
		StarResponse response = new StarResponse();
		response.setId(entity.getId());
		response.setStarName(entity.getStarName());
		return response;
	}

	private void updateFromRequest(Star entity, StarRequest request) {
		if (request.getStarName() != null) {
			entity.setStarName(request.getStarName());
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateStar(StarRequest request) {
		try {
			Star existing = starRepository.findByStarName(request.getStarName());
			if (existing != null) {
				existing.setStarName(request.getStarName());
				starRepository.save(existing);
				return new ResponseEntity("Star updated successfully", 200, mapToResponse(existing));
			} else {
				Star newEntity = new Star();
				updateFromRequest(newEntity, request);
				starRepository.save(newEntity);
				return new ResponseEntity("Star created successfully", 201, mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Star: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getStarById(Long id) {
		try {
			Star entity = starRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Star not found", 404, null);
			}
			return new ResponseEntity("Star found", 200, mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Star: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllStars() {
		try {
			List<Star> list = starRepository.findAll();
			List<StarResponse> responseList = list.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Stars fetched successfully", 200, responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Stars: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteStar(Long id) {
		try {
			Star entity = starRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Star not found", 404, null);
			}
			starRepository.delete(entity);
			return new ResponseEntity("Star deleted successfully", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Star: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadStarExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", 400, null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", 400, null);
			}

			List<Star> list = new ArrayList<>();
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

				Star entity = starRepository.findByStarName(name);
				if (entity == null) {
					entity = new Star();
					entity.setStarName(name);
					list.add(entity);
				}
			}

			if (!list.isEmpty()) {
				starRepository.saveAll(list);
			}

			return new ResponseEntity("Star data uploaded successfully", 200, list.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
		}
	}

}
