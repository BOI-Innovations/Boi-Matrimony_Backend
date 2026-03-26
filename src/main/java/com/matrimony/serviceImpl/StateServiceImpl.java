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

import com.matrimony.model.dto.request.StateRequest;
import com.matrimony.model.dto.response.StateResponse;
import com.matrimony.model.entity.Country;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.State;
import com.matrimony.repository.CountryRepository;
import com.matrimony.repository.StateRepository;
import com.matrimony.service.StateService;

@Service
public class StateServiceImpl implements StateService {

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CountryRepository countryRepository;

//	private StateResponse mapToResponse(State entity) {
//		List<DistrictResponse> districts = entity.getDistricts() != null ? entity.getDistricts().stream()
//				.map(d -> new DistrictResponse(d.getId(), d.getName(), entity.getId())).toList() : List.of();
//
//		return new StateResponse(entity.getId(), entity.getName(),
//				entity.getCountry() != null ? entity.getCountry().getId() : null, districts);
//	}

	private StateResponse mapToResponse(State entity) {
		return new StateResponse(entity.getId(), entity.getName(),
				entity.getCountry() != null ? entity.getCountry().getId() : null, null);
	}

	@Override
	@Transactional
	public ResponseEntity createState(StateRequest request) {
		try {
			Country country = countryRepository.findById(request.getCountryId()).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}

			State existing = stateRepository.findByNameAndCountryId(request.getName(), request.getCountryId());
			if (existing != null) {
				return new ResponseEntity("State already exists in this country", HttpStatus.BAD_REQUEST.value(), null);
			}

			State state = new State();
			state.setName(request.getName());
			state.setCountry(country);
			stateRepository.save(state);

			return new ResponseEntity("State created successfully", HttpStatus.CREATED.value(), mapToResponse(state));
		} catch (Exception e) {
			return new ResponseEntity("Error creating State: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateState(Long id, StateRequest request) {
		try {
			State state = stateRepository.findById(id).orElse(null);
			if (state == null) {
				return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
			}

			Country country = countryRepository.findById(request.getCountryId()).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}

			state.setName(request.getName());
			state.setCountry(country);
			stateRepository.save(state);

			return new ResponseEntity("State updated successfully", HttpStatus.OK.value(), mapToResponse(state));
		} catch (Exception e) {
			return new ResponseEntity("Error updating State: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getStateById(Long id) {
		try {
			State state = stateRepository.findById(id).orElse(null);
			if (state == null) {
				return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("State found", HttpStatus.OK.value(), mapToResponse(state));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching State: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllStates() {
		try {
			List<State> stateList = stateRepository.findAll();
			List<StateResponse> responseList = stateList.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All States fetched successfully", HttpStatus.OK.value(), responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching States: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getStatesByCountry(Long countryId) {
		try {
			List<State> stateList = stateRepository.findByCountryId(countryId);
			List<StateResponse> responseList = stateList.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("States fetched successfully", HttpStatus.OK.value(), responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching States: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteState(Long id) {
		try {
			State state = stateRepository.findById(id).orElse(null);
			if (state == null) {
				return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
			}

			stateRepository.delete(state);
			return new ResponseEntity("State deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting State: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadStateExcel(MultipartFile file, Long countryId) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			Country country = countryRepository.findById(countryId).orElse(null);
			if (country == null) {
				return new ResponseEntity("Country not found", HttpStatus.NOT_FOUND.value(), null);
			}

			Set<String> existingNames = stateRepository.findByCountryId(countryId).stream()
					.map(s -> s.getName().trim().toLowerCase()).collect(Collectors.toSet());

			List<State> newStates = new ArrayList<>();

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
					State state = new State();
					state.setName(name);
					state.setCountry(country);
					newStates.add(state);
					existingNames.add(lowerName);
				}
			}

			if (!newStates.isEmpty()) {
				stateRepository.saveAll(newStates);
			}

			return new ResponseEntity("State data uploaded successfully", HttpStatus.OK.value(),
					newStates.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}
}
