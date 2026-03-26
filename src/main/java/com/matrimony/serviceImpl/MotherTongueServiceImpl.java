package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.MotherTongueRequest;
import com.matrimony.model.dto.response.MotherTongueResponse;
import com.matrimony.model.entity.MotherTongue;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.MotherTongueRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.MotherTongueService;

@Service
public class MotherTongueServiceImpl implements MotherTongueService {

    @Autowired
    private MotherTongueRepository motherTongueRepository;

    @Autowired
    private UserRepository userRepository;

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return userPrincipal.getId();
	}
	
    private MotherTongueResponse mapToResponse(MotherTongue entity) {
        MotherTongueResponse response = new MotherTongueResponse();
        response.setId(entity.getId());
        response.setLanguageName(entity.getLanguageName());
        return response;
    }

    private void updateFromRequest(MotherTongue entity, MotherTongueRequest request) {
        if (request.getLanguageName() != null)
            entity.setLanguageName(request.getLanguageName());
    }

    @Override
    @Transactional
    public ResponseEntity createOrUpdateMotherTongue(MotherTongueRequest request) {
        try {
            Long userId = getCurrentUserId();
            User user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return new ResponseEntity("User not found", 404, null);

            MotherTongue existing = motherTongueRepository.findByLanguageName(request.getLanguageName());
            if (existing != null) {
                existing.setLanguageName(request.getLanguageName());
                motherTongueRepository.save(existing);
                return new ResponseEntity("Mother tongue updated successfully", 200, mapToResponse(existing));
            } else {
                MotherTongue newEntity = new MotherTongue();
                updateFromRequest(newEntity, request);
                motherTongueRepository.save(newEntity);
                return new ResponseEntity("Mother tongue created successfully", 201, mapToResponse(newEntity));
            }
        } catch (Exception e) {
            return new ResponseEntity("Error saving mother tongue: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getMotherTongueById(Long id) {
        try {
            MotherTongue entity = motherTongueRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("Mother tongue not found", 404, null);
            }
            return new ResponseEntity("Mother tongue found", 200, mapToResponse(entity));
        } catch (Exception e) {
            return new ResponseEntity("Error fetching mother tongue: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getAllMotherTongues() {
        try {
            List<MotherTongue> list = motherTongueRepository.findAll();
            List<MotherTongueResponse> responseList = list.stream().map(this::mapToResponse).toList();

            return new ResponseEntity("All mother tongues fetched successfully", 200, responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching mother tongues: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteMotherTongue(Long id) {
        try {
            MotherTongue entity = motherTongueRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("Mother tongue not found", 404, null);
            }
            motherTongueRepository.delete(entity);
            return new ResponseEntity("Mother tongue deleted successfully", 200, null);
        } catch (Exception e) {
            return new ResponseEntity("Error deleting mother tongue: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity uploadMotherTongueExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("File is empty", 400, null);
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return new ResponseEntity("No sheet found in file", 400, null);
            }

            List<MotherTongue> list = new ArrayList<>();

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

                MotherTongue entity = motherTongueRepository.findByLanguageName(name);
                if (entity == null) {
                    entity = new MotherTongue();
                    entity.setLanguageName(name);
                    list.add(entity);
                }
            }

            if (!list.isEmpty()) {
                motherTongueRepository.saveAll(list);
            }

            return new ResponseEntity("Mother tongue data uploaded successfully", 200, list.size() + " rows processed");

        } catch (Exception e) {
            return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
        }
    }
}
