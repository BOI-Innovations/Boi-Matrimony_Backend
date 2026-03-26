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

import com.matrimony.model.dto.request.WeightLbsRequest;
import com.matrimony.model.dto.response.WeightLbsResponse;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.model.entity.User;
import com.matrimony.model.entity.WeightLbs;
import com.matrimony.repository.UserRepository;
import com.matrimony.repository.WeightLbsRepository;
import com.matrimony.security.services.UserPrincipal;
import com.matrimony.service.WeightLbsService;

@Service
public class WeightLbsServiceImpl implements WeightLbsService {

    @Autowired
    private WeightLbsRepository weightLbsRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }

    private WeightLbsResponse mapToResponse(WeightLbs entity) {
        WeightLbsResponse response = new WeightLbsResponse();
        response.setId(entity.getId());
        response.setWeightInLbs(entity.getWeightInLbs());
        return response;
    }

    private void updateFromRequest(WeightLbs entity, WeightLbsRequest request) {
        if (request.getWeightInLbs() != null)
            entity.setWeightInLbs(request.getWeightInLbs());
    }

    @Override
    @Transactional
    public ResponseEntity createOrUpdateWeightLbs(WeightLbsRequest request) {
        try {
            Long userId = getCurrentUserId();
            User user = userRepository.findById(userId).orElse(null);
            if (user == null)
                return new ResponseEntity("User not found", 404, null);

            WeightLbs existing = weightLbsRepository.findByWeightInLbs(request.getWeightInLbs());
            if (existing != null) {
                existing.setWeightInLbs(request.getWeightInLbs());
                weightLbsRepository.save(existing);
                return new ResponseEntity("Weight (lbs) updated successfully", 200, mapToResponse(existing));
            } else {
                WeightLbs newEntity = new WeightLbs();
                updateFromRequest(newEntity, request);
                weightLbsRepository.save(newEntity);
                return new ResponseEntity("Weight (lbs) created successfully", 201, mapToResponse(newEntity));
            }
        } catch (Exception e) {
            return new ResponseEntity("Error saving Weight (lbs): " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getWeightLbsById(Long id) {
        try {
            WeightLbs entity = weightLbsRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("Weight (lbs) not found", 404, null);
            }
            return new ResponseEntity("Weight (lbs) found", 200, mapToResponse(entity));
        } catch (Exception e) {
            return new ResponseEntity("Error fetching Weight (lbs): " + e.getMessage(), 500, null);
        }
    }

    @Override
    public ResponseEntity getAllWeightLbs() {
        try {
            List<WeightLbs> list = weightLbsRepository.findAll();
            List<WeightLbsResponse> responseList = list.stream().map(this::mapToResponse).toList();

            return new ResponseEntity("All Weight (lbs) fetched successfully", 200, responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching Weight (lbs): " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteWeightLbs(Long id) {
        try {
            WeightLbs entity = weightLbsRepository.findById(id).orElse(null);
            if (entity == null) {
                return new ResponseEntity("Weight (lbs) not found", 404, null);
            }
            weightLbsRepository.delete(entity);
            return new ResponseEntity("Weight (lbs) deleted successfully", 200, null);
        } catch (Exception e) {
            return new ResponseEntity("Error deleting Weight (lbs): " + e.getMessage(), 500, null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity uploadWeightLbsExcel(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("File is empty", 400, null);
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return new ResponseEntity("No sheet found in file", 400, null);
            }

            List<WeightLbs> list = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                Cell valueCell = row.getCell(1);

                if (valueCell == null || valueCell.getCellType() != CellType.STRING) {
                    continue;
                }

                String value = valueCell.getStringCellValue().trim();
                if (value.isEmpty())
                    continue;

                WeightLbs entity = weightLbsRepository.findByWeightInLbs(value);
                if (entity == null) {
                    entity = new WeightLbs();
                    entity.setWeightInLbs(value);
                    list.add(entity);
                }
            }

            if (!list.isEmpty()) {
                weightLbsRepository.saveAll(list);
            }

            return new ResponseEntity("Weight (lbs) data uploaded successfully", 200, list.size() + " rows processed");

        } catch (Exception e) {
            return new ResponseEntity("Error processing Excel file: " + e.getMessage(), 500, null);
        }
    }
}
