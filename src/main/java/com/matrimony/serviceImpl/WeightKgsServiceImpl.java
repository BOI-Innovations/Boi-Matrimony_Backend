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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.WeightKgsRequest;
import com.matrimony.model.dto.response.WeightKgsResponse;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.WeightKgs;
import com.matrimony.repository.WeightKgsRepository;
import com.matrimony.service.WeightKgsService;

@Service
public class WeightKgsServiceImpl implements WeightKgsService {

	@Autowired
	private WeightKgsRepository weightKgsRepository;

	private WeightKgsResponse mapToResponse(WeightKgs entity) {
		WeightKgsResponse response = new WeightKgsResponse();
		response.setId(entity.getId());
		response.setWeightInKgs(entity.getWeightInKgs());
		return response;
	}

	private void updateFromRequest(WeightKgs entity, WeightKgsRequest request) {
		if (request.getWeightInKgs() != null)
			entity.setWeightInKgs(request.getWeightInKgs());
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateWeight(WeightKgsRequest request) {
		try {
			WeightKgs existing = weightKgsRepository.findByWeightInKgs(request.getWeightInKgs());
			if (existing != null) {
				existing.setWeightInKgs(request.getWeightInKgs());
				weightKgsRepository.save(existing);
				return new ResponseEntity("Weight updated successfully", 200, mapToResponse(existing));
			} else {
				WeightKgs newEntity = new WeightKgs();
				updateFromRequest(newEntity, request);
				weightKgsRepository.save(newEntity);
				return new ResponseEntity("Weight created successfully", 201, mapToResponse(newEntity));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving Weight: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getWeightById(Long id) {
		try {
			WeightKgs entity = weightKgsRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Weight not found", 404, null);
			}
			return new ResponseEntity("Weight found", 200, mapToResponse(entity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Weight: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllWeights() {
	    try {
	        List<WeightKgs> list = weightKgsRepository.findAll();

	        list.sort(Comparator.comparingInt(w -> 
	            Integer.parseInt(w.getWeightInKgs().replace(" kg", ""))
	        ));

	        List<WeightKgsResponse> responseList = list.stream()
	                                                   .map(this::mapToResponse)
	                                                   .toList();

	        return new ResponseEntity("All Weights fetched successfully", 200, responseList);
	    } catch (Exception e) {
	        return new ResponseEntity("Error fetching Weights: " + e.getMessage(), 500, null);
	    }
	}


	@Override
	@Transactional
	public ResponseEntity deleteWeight(Long id) {
		try {
			WeightKgs entity = weightKgsRepository.findById(id).orElse(null);
			if (entity == null) {
				return new ResponseEntity("Weight not found", 404, null);
			}
			weightKgsRepository.delete(entity);
			return new ResponseEntity("Weight deleted successfully", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Weight: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadWeightExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", 400, null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", 400, null);
			}

			List<WeightKgs> list = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell weightCell = row.getCell(1);

				if (weightCell == null || weightCell.getCellType() != CellType.STRING) {
					continue;
				}

				String weightInKgs = weightCell.getStringCellValue().trim();
				if (weightInKgs.isEmpty())
					continue;

				WeightKgs entity = weightKgsRepository.findByWeightInKgs(weightInKgs);
				if (entity == null) {
					entity = new WeightKgs();
					entity.setWeightInKgs(weightInKgs);
					list.add(entity);
				}
			}

			if (!list.isEmpty()) {
				weightKgsRepository.saveAll(list);
			}

			return new ResponseEntity("Weight data uploaded successfully", 200, list.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
		}
	}

}
