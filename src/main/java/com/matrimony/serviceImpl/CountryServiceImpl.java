package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.matrimony.model.dto.request.CountryRequest;
import com.matrimony.model.dto.response.CountryResponse;
import com.matrimony.model.dto.response.StateResponse;
import com.matrimony.model.entity.Country;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.CountryRepository;
import com.matrimony.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository countryRepository;

//	private CountryResponse mapToResponse(Country entity) {
//		List<StateResponse> states = entity
//				.getStates() != null
//						? entity.getStates().stream()
//								.map(s -> new StateResponse(s.getId(), s.getName(), entity.getId(),
//										s.getDistricts() != null
//												? s.getDistricts().stream()
//														.map(d -> new DistrictResponse(d.getId(), d.getName(),
//																s.getId()))
//														.toList()
//												: List.of()))
//								.toList()
//						: List.of();
//
//		return new CountryResponse(entity.getId(), entity.getCountryName(), states);
//	}

	private CountryResponse mapToResponse(Country entity) {
		List<StateResponse> states = entity.getStates() != null
				? entity.getStates().stream()
						.map(s -> new StateResponse(s.getId(), s.getName(), entity.getId(), List.of())).toList()
				: List.of();

		return new CountryResponse(entity.getId(), entity.getCountryName(), states);
	}

	@Override
	@Transactional
	public ResponseEntity createCountry(CountryRequest request) {
		try {
			Country existing = countryRepository.findByCountryName(request.getCountryName());
			if (existing != null) {
				return new ResponseEntity("Country already exists", HttpStatus.BAD_REQUEST.value(), null);
			}

			Country country = new Country();
			country.setCountryName(request.getCountryName());
			countryRepository.save(country);

			return new ResponseEntity("Country created successfully", HttpStatus.CREATED.value(),
					mapToResponse(country));
		} catch (Exception e) {
			return new ResponseEntity("Error creating Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateCountry(Long id, CountryRequest request) {
		try {
			Country country = countryRepository.findById(id).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}

			country.setCountryName(request.getCountryName());
			countryRepository.save(country);

			return new ResponseEntity("Country updated successfully", HttpStatus.OK.value(), mapToResponse(country));
		} catch (Exception e) {
			return new ResponseEntity("Error updating Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getCountryById(Long id) {
		try {
			Country country = countryRepository.findById(id).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Country found", HttpStatus.OK.value(), mapToResponse(country));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllCountries() {
		try {
			List<Country> countryList = countryRepository.findAll();
			List<CountryResponse> responseList = countryList.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Countries fetched successfully", HttpStatus.OK.value(), responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Countries: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteCountry(Long id) {
		try {
			Country country = countryRepository.findById(id).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}

			countryRepository.delete(country);
			return new ResponseEntity("Country deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Country: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadCountryExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			Set<String> existingNames = countryRepository.findAll().stream()
					.map(c -> c.getCountryName().trim().toLowerCase()).collect(Collectors.toSet());

			List<Country> newCountries = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell cell = row.getCell(1);
				if (cell == null || cell.getCellType() != CellType.STRING)
					continue;

				String name = cell.getStringCellValue().trim();
				if (name.isEmpty())
					continue;

				String lowerName = name.toLowerCase();
				if (!existingNames.contains(lowerName)) {
					Country country = new Country();
					country.setCountryName(name);
					newCountries.add(country);
					existingNames.add(lowerName);
				}
			}

			if (!newCountries.isEmpty()) {
				countryRepository.saveAll(newCountries);
			}

			return new ResponseEntity("Country data uploaded successfully", HttpStatus.OK.value(),
					newCountries.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}
}
