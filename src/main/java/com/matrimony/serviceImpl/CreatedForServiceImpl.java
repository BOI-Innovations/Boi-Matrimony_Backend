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

import com.matrimony.model.dto.request.CreatedForRequest;
import com.matrimony.model.dto.response.CreatedForResponse;
import com.matrimony.model.entity.CreatedFor;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.CreatedForRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.CreatedForService;

@Service
public class CreatedForServiceImpl implements CreatedForService {

    @Autowired
    private CreatedForRepository createdForRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }

    private CreatedForResponse mapToResponse(CreatedFor entity) {
        CreatedForResponse response = new CreatedForResponse();
        response.setId(entity.getId());
        response.setTargetPerson(entity.getTargetPerson());
        return response;
    }

    private void updateFromRequest(CreatedFor entity, CreatedForRequest request) {
        if (request.getTargetPerson() != null)
            entity.setTargetPerson(request.getTargetPerson());
    }

    @Override
    @Transactional
    public ResponseEntity createOrUpdateCreatedFor(CreatedForRequest request) {
        try {
            Long userId = getCurrentUserId();
            User user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return new ResponseEntity("User not found", 404, null);

            CreatedFor existing = createdForRepository.findByTargetPerson(request.getTargetPerson());
            if (existing != null) {
                existing.setTargetPerson(request.getTargetPerson());
                createdForRepository.save(existing);
                return new ResponseEntity("CreatedFor updated successfully", 200, mapToResponse(existing));
            } else {
                CreatedFor newEntity = new CreatedFor();
                updateFromRequest(newEntity, request);
                createdForRepository.save(newEntity);
                return new ResponseEntity("CreatedFor created successfully", 201, mapToResponse(newEntity));
            }
        } catch (Exception e) {
            return new ResponseEntity("Error saving CreatedFor: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getCreatedForById(Long id) {
        try {
            CreatedFor entity = createdForRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("CreatedFor not found", 404, null);
            }
            return new ResponseEntity("CreatedFor found", 200, mapToResponse(entity));
        } catch (Exception e) {
            return new ResponseEntity("Error fetching CreatedFor: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getAllCreatedFor() {
        try {
            List<CreatedFor> list = createdForRepository.findAll();
            List<CreatedForResponse> responseList = list.stream().map(this::mapToResponse).toList();

            return new ResponseEntity("All CreatedFor fetched successfully", 200, responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching CreatedFor: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteCreatedFor(Long id) {
        try {
            CreatedFor entity = createdForRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("CreatedFor not found", 404, null);
            }
            createdForRepository.delete(entity);
            return new ResponseEntity("CreatedFor deleted successfully", 200, null);
        } catch (Exception e) {
            return new ResponseEntity("Error deleting CreatedFor: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity uploadCreatedForExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("File is empty", 400, null);
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return new ResponseEntity("No sheet found in file", 400, null);
            }

            List<CreatedFor> list = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Cell nameCell = row.getCell(1);

                if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                    continue;
                }

                String targetPerson = nameCell.getStringCellValue().trim();
                if (targetPerson.isEmpty())
                    continue;

                CreatedFor entity = createdForRepository.findByTargetPerson(targetPerson);
                if (entity == null) {
                    entity = new CreatedFor();
                    entity.setTargetPerson(targetPerson);
                    list.add(entity);
                }
            }

            if (!list.isEmpty()) {
                createdForRepository.saveAll(list);
            }

            return new ResponseEntity("CreatedFor data uploaded successfully", 200, list.size() + " rows processed");

        } catch (Exception e) {
            return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
        }
    }
}
