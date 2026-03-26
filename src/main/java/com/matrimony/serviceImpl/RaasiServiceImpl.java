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

import com.matrimony.model.dto.request.RaasiRequest;
import com.matrimony.model.dto.response.RaasiResponse;
import com.matrimony.model.entity.Raasi;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.RaasiRepository;
import com.matrimony.service.RaasiService;

@Service
public class RaasiServiceImpl implements RaasiService {

	@Autowired
	private RaasiRepository raasiRepository;

	private RaasiResponse mapToResponse(Raasi entity) {
		return new RaasiResponse(entity.getId(), entity.getName());
	}

	@Override
	@Transactional
	public ResponseEntity createRaasi(RaasiRequest request) {
		try {
			Raasi existing = raasiRepository.findByName(request.getName());
			if (existing != null) {
				return new ResponseEntity("Raasi already exists", HttpStatus.BAD_REQUEST.value(), null);
			}

			Raasi raasi = new Raasi();
			raasi.setName(request.getName());
			raasiRepository.save(raasi);

			return new ResponseEntity("Raasi created successfully", HttpStatus.CREATED.value(), mapToResponse(raasi));
		} catch (Exception e) {
			return new ResponseEntity("Error creating Raasi: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateRaasi(Long id, RaasiRequest request) {
		try {
			Raasi raasi = raasiRepository.findById(id).orElse(null);
			if (raasi == null) {
				return new ResponseEntity("Raasi not found", HttpStatus.NOT_FOUND.value(), null);
			}

			raasi.setName(request.getName());
			raasiRepository.save(raasi);

			return new ResponseEntity("Raasi updated successfully", HttpStatus.OK.value(), mapToResponse(raasi));
		} catch (Exception e) {
			return new ResponseEntity("Error updating Raasi: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getRaasiById(Long id) {
		try {
			Raasi raasi = raasiRepository.findById(id).orElse(null);
			if (raasi == null) {
				return new ResponseEntity("Raasi not found", HttpStatus.NOT_FOUND.value(), null);
			}
			return new ResponseEntity("Raasi found", HttpStatus.OK.value(), mapToResponse(raasi));
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Raasi: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllRaasis() {
		try {
			List<Raasi> raasiList = raasiRepository.findAll();
			List<RaasiResponse> responseList = raasiList.stream().map(this::mapToResponse).toList();
			return new ResponseEntity("All Raasis fetched successfully", HttpStatus.OK.value(), responseList);
		} catch (Exception e) {
			return new ResponseEntity("Error fetching Raasis: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteRaasi(Long id) {
		try {
			Raasi raasi = raasiRepository.findById(id).orElse(null);
			if (raasi == null) {
				return new ResponseEntity("Raasi not found", HttpStatus.NOT_FOUND.value(), null);
			}

			raasiRepository.delete(raasi);
			return new ResponseEntity("Raasi deleted successfully", HttpStatus.OK.value(), null);
		} catch (Exception e) {
			return new ResponseEntity("Error deleting Raasi: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity uploadRaasiExcel(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
		}

		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
			}

			Set<String> existingRaasiNames = raasiRepository.findAll().stream()
					.map(r -> r.getName().trim().toLowerCase()).collect(Collectors.toSet());

			List<Raasi> newRaasis = new ArrayList<>();

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
				if (!existingRaasiNames.contains(lowerName)) {
					Raasi raasi = new Raasi();
					raasi.setName(name);
					newRaasis.add(raasi);
					existingRaasiNames.add(lowerName);
				}
			}

			if (!newRaasis.isEmpty()) {
				raasiRepository.saveAll(newRaasis);
			}

			return new ResponseEntity("Raasi data uploaded successfully", HttpStatus.OK.value(),
					newRaasis.size() + " rows processed");

		} catch (Exception e) {
			return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

}
