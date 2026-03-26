package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
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

import com.matrimony.model.dto.request.OccupationCategoryRequest;
import com.matrimony.model.dto.request.OccupationOptionRequest;
import com.matrimony.model.dto.response.OccupationCategoryResponse;
import com.matrimony.model.dto.response.OccupationOptionResponse;
import com.matrimony.model.entity.OccupationCategory;
import com.matrimony.model.entity.OccupationOption;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.OccupationCategoryRepository;
import com.matrimony.repository.OccupationOptionRepository;
import com.matrimony.service.OccupationService;

@Service
public class OccupationServiceImpl implements OccupationService {

	@Autowired
	private OccupationCategoryRepository occupationCategoryRepository;

	@Autowired
	private OccupationOptionRepository occupationOptionRepository;

	private OccupationCategoryResponse mapToCategoryResponse(OccupationCategory category) {
		OccupationCategoryResponse response = new OccupationCategoryResponse();
		response.setId(category.getId());
		response.setCategoryName(category.getCategoryName());
		response.setOccupationOptions(
				category.getOccupationOptions().stream().map(this::mapToOptionResponse).collect(Collectors.toList()));
		return response;
	}

	private OccupationOptionResponse mapToOptionResponse(OccupationOption option) {
		OccupationOptionResponse response = new OccupationOptionResponse();
		response.setId(option.getId());
		response.setOptionName(option.getOptionName());
		return response;
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateCategory(OccupationCategoryRequest request) {
		try {
			OccupationCategory category = new OccupationCategory();
			category.setCategoryName(request.getCategoryName());
			occupationCategoryRepository.save(category);
			return new ResponseEntity("Occupation Category created successfully", HttpStatus.CREATED.value(),
					mapToCategoryResponse(category));
		} catch (Exception e) {
			return new ResponseEntity("Error saving Occupation Category: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateOption(OccupationOptionRequest request) {
		try {
			OccupationCategory category = occupationCategoryRepository.findById(request.getCategoryId()).orElse(null);
			if (category == null) {
				return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND.value(), null);
			}
			OccupationOption option = new OccupationOption();
			option.setOptionName(request.getOptionName());
			option.setCategory(category);
			occupationOptionRepository.save(option);
			return new ResponseEntity("Occupation Option created successfully", HttpStatus.CREATED.value(),
					mapToOptionResponse(option));
		} catch (Exception e) {
			return new ResponseEntity("Error saving Occupation Option: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getCategoryById(Long id) {
		try {
			OccupationCategory category = occupationCategoryRepository.findById(id).orElse(null);
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
			List<OccupationCategory> categories = occupationCategoryRepository.findAll(Sort.by(Sort.Order.asc("id")));
			if (categories.isEmpty()) {
				return new ResponseEntity("No categories found", HttpStatus.NOT_FOUND.value(), null);
			}
			List<OccupationCategoryResponse> responses = categories.stream().map(this::mapToCategoryResponse)
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
			List<OccupationOption> options = occupationOptionRepository.findByCategoryId(categoryId);
			if (options.isEmpty()) {
				return new ResponseEntity("No options found for this category", HttpStatus.NOT_FOUND.value(), null);
			}
			List<OccupationOptionResponse> responses = options.stream().map(this::mapToOptionResponse)
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
			OccupationCategory category = occupationCategoryRepository.findById(id).orElse(null);
			if (category == null) {
				return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND.value(), null);
			}
			occupationCategoryRepository.delete(category);
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
			OccupationOption option = occupationOptionRepository.findById(id).orElse(null);
			if (option == null) {
				return new ResponseEntity("Option not found", HttpStatus.NOT_FOUND.value(), null);
			}
			occupationOptionRepository.delete(option);
			return new ResponseEntity("Option deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Option: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadOccupationCategoryExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found", HttpStatus.BAD_REQUEST.value(), null);
			}

			Set<String> existingNames = occupationCategoryRepository.findAll().stream()
					.map(c -> c.getCategoryName().trim().toLowerCase()).collect(Collectors.toSet());

			List<OccupationCategory> newCategories = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);
				if (nameCell == null)
					continue;

				String categoryName = nameCell.getStringCellValue().trim();
				if (categoryName.isEmpty() || existingNames.contains(categoryName.toLowerCase()))
					continue;

				OccupationCategory category = new OccupationCategory();
				category.setCategoryName(categoryName);
				newCategories.add(category);
				existingNames.add(categoryName.toLowerCase());
			}

			if (!newCategories.isEmpty()) {
				occupationCategoryRepository.saveAll(newCategories);
			}

			return new ResponseEntity("Occupation categories uploaded successfully", HttpStatus.OK.value(),
					newCategories.size() + " categories saved");

		} catch (Exception e) {
			return new ResponseEntity("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadOccupationOptionExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found", HttpStatus.BAD_REQUEST.value(), null);
			}

			Set<String> existingNames = occupationOptionRepository.findAll().stream()
					.map(o -> o.getOptionName().trim().toLowerCase()).collect(Collectors.toSet());

			List<OccupationOption> newOptions = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);
				Cell categoryCell = row.getCell(2);
				if (nameCell == null || categoryCell == null)
					continue;

				String optionName = nameCell.getStringCellValue().trim();
				if (optionName.isEmpty())
					continue;

				Long categoryId = (long) categoryCell.getNumericCellValue();
				Optional<OccupationCategory> categoryOpt = occupationCategoryRepository.findById(categoryId);
				if (categoryOpt.isEmpty())
					continue;

				if (!existingNames.contains(optionName.toLowerCase())) {
					OccupationOption option = new OccupationOption();
					option.setOptionName(optionName);
					option.setCategory(categoryOpt.get());
					newOptions.add(option);
					existingNames.add(optionName.toLowerCase());
				}
			}

			if (!newOptions.isEmpty()) {
				occupationOptionRepository.saveAll(newOptions);
			}

			return new ResponseEntity("Occupation options uploaded successfully", HttpStatus.OK.value(),
					newOptions.size() + " options saved");

		} catch (Exception e) {
			return new ResponseEntity("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}
