package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.EducationCategoryRequest;
import com.matrimony.model.dto.request.EducationOptionRequest;
import com.matrimony.model.dto.response.EducationCategoryResponse;
import com.matrimony.model.dto.response.EducationOptionResponse;
import com.matrimony.model.entity.EducationCategory;
import com.matrimony.model.entity.EducationOption;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.EducationCategoryRepository;
import com.matrimony.repository.EducationOptionRepository;
import com.matrimony.service.EducationService;

@Service
public class EducationServiceImpl implements EducationService {

	@Autowired
	private EducationCategoryRepository educationCategoryRepository;

	@Autowired
	private EducationOptionRepository educationOptionRepository;

	private EducationCategoryResponse mapToCategoryResponse(EducationCategory category) {
		EducationCategoryResponse response = new EducationCategoryResponse();
		response.setId(category.getId());
		response.setCategoryName(category.getCategoryName());
		response.setEducationOptions(
				category.getEducationOptions().stream().map(this::mapToOptionResponse).collect(Collectors.toList()));
		return response;
	}

	private EducationOptionResponse mapToOptionResponse(EducationOption option) {
		EducationOptionResponse response = new EducationOptionResponse();
		response.setId(option.getId());
		response.setOptionName(option.getOptionName());
		return response;
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateCategory(EducationCategoryRequest request) {
		try {
			EducationCategory category = new EducationCategory();
			category.setCategoryName(request.getCategoryName());
			educationCategoryRepository.save(category);
			return new ResponseEntity("Category created successfully", HttpStatus.CREATED.value(),
					mapToCategoryResponse(category));
		} catch (Exception e) {
			return new ResponseEntity("Error saving Category: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateOption(EducationOptionRequest request) {
		try {
			EducationCategory category = educationCategoryRepository.findById(request.getCategoryId()).orElse(null);
			if (category == null) {
				return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND.value(), null);
			}
			EducationOption option = new EducationOption();
			option.setOptionName(request.getOptionName());
			option.setCategory(category);
			educationOptionRepository.save(option);
			return new ResponseEntity("Option created successfully", HttpStatus.CREATED.value(),
					mapToOptionResponse(option));
		} catch (Exception e) {
			return new ResponseEntity("Error saving Option: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getCategoryById(Long id) {
		try {
			EducationCategory category = educationCategoryRepository.findById(id).orElse(null);
			if (category == null) {
				return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Category found", HttpStatus.OK.value(), mapToCategoryResponse(category));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Category: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllCategories() {
		try {
			Sort sort = Sort.by(Sort.Order.asc("id"));
			List<EducationCategory> categories = educationCategoryRepository.findAll(sort);

			if (categories.isEmpty()) {
				return new ResponseEntity("No categories found", HttpStatus.NOT_FOUND.value(), null);
			}
			List<EducationCategoryResponse> responses = categories.stream().map(this::mapToCategoryResponse)
					.collect(Collectors.toList());
			return new ResponseEntity("Categories fetched successfully", HttpStatus.OK.value(), responses);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Categories: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getOptionsByCategory(Long categoryId) {
		try {
			List<EducationOption> options = educationOptionRepository.findByCategoryId(categoryId);

			if (options.isEmpty()) {
				return new ResponseEntity("No options found for the given category", HttpStatus.NOT_FOUND.value(),
						null);
			}

			List<EducationOptionResponse> responses = options.stream().map(this::mapToOptionResponse)
					.collect(Collectors.toList());

			return new ResponseEntity("Options fetched successfully", HttpStatus.OK.value(), responses);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Options: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteCategory(Long id) {
		try {
			EducationCategory category = educationCategoryRepository.findById(id).orElse(null);
			if (category == null) {
				return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND.value(), null);
			}
			educationCategoryRepository.delete(category);
			return new ResponseEntity("Category deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Category: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteOption(Long id) {
		try {
			EducationOption option = educationOptionRepository.findById(id).orElse(null);
			if (option == null) {
				return new ResponseEntity("Option not found", HttpStatus.NOT_FOUND.value(), null);
			}
			educationOptionRepository.delete(option);
			return new ResponseEntity("Option deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Option: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadEducationCategoryExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			Set<String> existingNames = educationCategoryRepository.findAll().stream()
					.map(c -> c.getCategoryName().trim().toLowerCase()).collect(Collectors.toSet());

			List<EducationCategory> newCategories = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);
				if (nameCell == null || nameCell.getCellType() != CellType.STRING)
					continue;

				String categoryName = nameCell.getStringCellValue().trim();
				if (categoryName.isEmpty())
					continue;

				String lowerName = categoryName.toLowerCase();
				if (!existingNames.contains(lowerName)) {
					EducationCategory category = new EducationCategory();
					category.setCategoryName(categoryName);
					newCategories.add(category);
					existingNames.add(lowerName);
				}
			}

			if (!newCategories.isEmpty()) {
				educationCategoryRepository.saveAll(newCategories);
			}

			return new ResponseEntity("Education category data uploaded successfully", HttpStatus.OK.value(),
					newCategories.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadEducationOptionExcel(MultipartFile file) {
	    if (file == null || file.isEmpty()) {
	        return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
	    }

	    try (InputStream inputStream = file.getInputStream();
	         Workbook workbook = new XSSFWorkbook(inputStream)) {

	        Sheet sheet = workbook.getSheetAt(0);
	        if (sheet == null) {
	            return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
	        }

	        Set<String> existingOptionNames = educationOptionRepository.findAll().stream()
	                .map(o -> o.getOptionName().trim().toLowerCase())
	                .collect(Collectors.toSet());

	        List<EducationOption> newOptions = new ArrayList<>();

	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;

	            Cell optionCell = row.getCell(1);
	            if (optionCell == null || optionCell.getCellType() != CellType.STRING) continue;

	            String optionName = optionCell.getStringCellValue().trim();
	            if (optionName.isEmpty()) continue;

	            Cell categoryIdCell = row.getCell(2);
	            if (categoryIdCell == null || categoryIdCell.getCellType() != CellType.NUMERIC) continue;

	            Long categoryId = (long) categoryIdCell.getNumericCellValue();

	            Optional<EducationCategory> categoryOpt = educationCategoryRepository.findById(categoryId);
	            if (categoryOpt.isEmpty()) {
	                continue; 
	            }

	            String lowerName = optionName.toLowerCase();
	            if (!existingOptionNames.contains(lowerName)) {
	                EducationOption option = new EducationOption();
	                option.setOptionName(optionName);
	                option.setCategory(categoryOpt.get());
	                newOptions.add(option);
	                existingOptionNames.add(lowerName);
	            }
	        }

	        if (!newOptions.isEmpty()) {
	            educationOptionRepository.saveAll(newOptions);
	        }

	        return new ResponseEntity(
	                "Education options uploaded successfully",
	                HttpStatus.OK.value(),
	                newOptions.size() + " rows processed"
	        );

	    } catch (Exception e) {
	        return new ResponseEntity(
	                "Error processing Excel file: " + e.getMessage(),
	                HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                null
	        );
	    }
	}

}
