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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.CasteRequest;
import com.matrimony.model.dto.response.CasteResponse;
import com.matrimony.model.entity.Caste;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.CasteRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.CasteService;

@Service
public class CasteServiceImpl implements CasteService {

	@Autowired
	private CasteRepository casteRepository;

	@Autowired
	private UserRepository userRepository;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}

	private CasteResponse mapToResponse(Caste casteEntity) {
		CasteResponse response = new CasteResponse();
		response.setId(casteEntity.getId());
		response.setCasteName(casteEntity.getCasteName());
		return response;
	}

	private void updateFromRequest(Caste casteEntity, CasteRequest request) {
		if (request.getCasteName() != null)
			casteEntity.setCasteName(request.getCasteName());
	}

	@Override
	@Transactional
	public ResponseEntity createOrUpdateCaste(CasteRequest request) {
		try {
			Long userId = getCurrentUserId();
			User user = userRepository.findById(userId).orElse(null);
			if (user == null)
				return new ResponseEntity("User not found", 404, null);

			Caste existingCaste = casteRepository.findByCasteName(request.getCasteName());
			if (existingCaste != null) {
				existingCaste.setCasteName(request.getCasteName());
				casteRepository.save(existingCaste);
				return new ResponseEntity("Caste updated successfully", 200, mapToResponse(existingCaste));
			} else {
				Caste newCaste = new Caste();
				updateFromRequest(newCaste, request);
				casteRepository.save(newCaste);
				return new ResponseEntity("Caste created successfully", 201, mapToResponse(newCaste));
			}
		} catch (Exception e) {
			return new ResponseEntity("Error saving caste: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getCasteById(Long id) {
		try {
			Caste casteEntity = casteRepository.findById(id).orElse(null);
			if (casteEntity == null) {
				return new ResponseEntity("Caste not found", 404, null);
			}
			return new ResponseEntity("Caste found", 200, mapToResponse(casteEntity));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching caste: " + e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity getAllCastes() {
		try {
			List<Caste> casteList = casteRepository.findAll();
			List<CasteResponse> responseList = casteList.stream().map(this::mapToResponse).toList();

			return new ResponseEntity("All castes fetched successfully", 200, responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching castes: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteCaste(Long id) {
		try {
			Caste casteEntity = casteRepository.findById(id).orElse(null);
			if (casteEntity == null) {
				return new ResponseEntity("Caste not found", 404, null);
			}
			casteRepository.delete(casteEntity);
			return new ResponseEntity("Caste deleted successfully", 200, null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting caste: " + e.getMessage(), 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadCasteExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", 400, null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", 400, null);
			}

			List<Caste> casteList = new ArrayList<>();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell nameCell = row.getCell(1);

				if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
					continue;
				}

				String name = nameCell.getStringCellValue().trim();
				if (name.isEmpty())
					continue;

				Caste caste = casteRepository.findByCasteName(name);
				if (caste == null) {
					caste = new Caste();
					caste.setCasteName(name);
					casteList.add(caste);
				}
			}

			if (!casteList.isEmpty()) {
				casteRepository.saveAll(casteList);
			}

			return new ResponseEntity("Caste data uploaded successfully", 200, casteList.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
		}
	}
}
