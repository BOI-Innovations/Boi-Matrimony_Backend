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

import com.matrimony.model.dto.request.SubCasteRequest;
import com.matrimony.model.dto.response.SubCasteResponse;
import com.matrimony.model.entity.SubCaste;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.repository.SubCasteRepository;
import com.matrimony.repository.UserRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.SubCasteService;

@Service
public class SubCasteServiceImpl implements SubCasteService {

    @Autowired
    private SubCasteRepository subCasteRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }

    private SubCasteResponse mapToResponse(SubCaste entity) {
        SubCasteResponse response = new SubCasteResponse();
        response.setId(entity.getId());
        response.setSubCasteName(entity.getSubCasteName());
        return response;
    }

    private void updateFromRequest(SubCaste entity, SubCasteRequest request) {
        if (request.getSubCasteName() != null)
            entity.setSubCasteName(request.getSubCasteName());
    }

    @Override
    @Transactional
    public ResponseEntity createOrUpdateSubCaste(SubCasteRequest request) {
        try {
            Long userId = getCurrentUserId();
            User user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return new ResponseEntity("User not found", 404, null);

            SubCaste existing = subCasteRepository.findBySubCasteName(request.getSubCasteName());
            if (existing != null) {
                existing.setSubCasteName(request.getSubCasteName());
                subCasteRepository.save(existing);
                return new ResponseEntity("SubCaste updated successfully", 200, mapToResponse(existing));
            } else {
                SubCaste newEntity = new SubCaste();
                updateFromRequest(newEntity, request);
                subCasteRepository.save(newEntity);
                return new ResponseEntity("SubCaste created successfully", 201, mapToResponse(newEntity));
            }
        } catch (Exception e) {
            return new ResponseEntity("Error saving subCaste: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getSubCasteById(Long id) {
        try {
            SubCaste entity = subCasteRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("SubCaste not found", 404, null);
            }
            return new ResponseEntity("SubCaste found", 200, mapToResponse(entity));
        } catch (Exception e) {
            return new ResponseEntity("Error fetching subCaste: " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getAllSubCastes() {
        try {
            List<SubCaste> list = subCasteRepository.findAll();
            List<SubCasteResponse> responseList = list.stream().map(this::mapToResponse).toList();
            return new ResponseEntity("All subCastes fetched successfully", 200, responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching subCastes: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteSubCaste(Long id) {
        try {
            SubCaste entity = subCasteRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("SubCaste not found", 404, null);
            }
            subCasteRepository.delete(entity);
            return new ResponseEntity("SubCaste deleted successfully", 200, null);
        } catch (Exception e) {
            return new ResponseEntity("Error deleting subCaste: " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity uploadSubCasteExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("File is empty", 400, null);
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return new ResponseEntity("No sheet found in file", 400, null);
            }

            List<SubCaste> subCastes = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nameCell = row.getCell(1); // Assuming col 1 = subCasteName
                if (nameCell == null || nameCell.getCellType() != CellType.STRING) continue;

                String subCasteName = nameCell.getStringCellValue().trim();
                if (subCasteName.isEmpty()) continue;

                SubCaste existing = subCasteRepository.findBySubCasteName(subCasteName);
                if (existing == null) {
                    SubCaste subCaste = new SubCaste();
                    subCaste.setSubCasteName(subCasteName);
                    subCastes.add(subCaste);
                }
            }

            if (!subCastes.isEmpty()) {
                subCasteRepository.saveAll(subCastes);
            }

            return new ResponseEntity("SubCaste data uploaded successfully", 200, subCastes.size() + " rows processed");

        } catch (Exception e) {
            return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
        }
    }
}
