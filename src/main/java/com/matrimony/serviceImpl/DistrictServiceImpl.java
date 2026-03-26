package com.matrimony.serviceImpl;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrimony.model.dto.request.DistrictRequest;
import com.matrimony.model.dto.response.DistrictResponse;
import com.matrimony.model.entity.State;
import com.matrimony.model.entity.District;
import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.repository.StateRepository;
import com.matrimony.repository.DistrictRepository;
import com.matrimony.service.DistrictService;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private StateRepository stateRepository;

    private DistrictResponse mapToResponse(District entity) {
        return new DistrictResponse(entity.getId(), entity.getName(), entity.getState().getId());
    }

    @Override
    @Transactional
    public ResponseEntity createDistrict(DistrictRequest request) {
        try {
            State state = stateRepository.findById(request.getStateId()).orElse(null);
            if (state == null) {
                return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
            }

            District existing = districtRepository.findByNameAndStateId(request.getName(), request.getStateId());
            if (existing != null) {
                return new ResponseEntity("District already exists in this state", HttpStatus.BAD_REQUEST.value(), null);
            }

            District district = new District();
            district.setName(request.getName());
            district.setState(state);
            districtRepository.save(district);

            return new ResponseEntity("District created successfully", HttpStatus.CREATED.value(), mapToResponse(district));
        } catch (Exception e) {
            return new ResponseEntity("Error creating District: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity updateDistrict(Long id, DistrictRequest request) {
        try {
            District district = districtRepository.findById(id).orElse(null);
            if (district == null) {
                return new ResponseEntity("District not found", HttpStatus.NOT_FOUND.value(), null);
            }

            State state = stateRepository.findById(request.getStateId()).orElse(null);
            if (state == null) {
                return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
            }

            district.setName(request.getName());
            district.setState(state);
            districtRepository.save(district);

            return new ResponseEntity("District updated successfully", HttpStatus.OK.value(), mapToResponse(district));
        } catch (Exception e) {
            return new ResponseEntity("Error updating District: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public ResponseEntity getDistrictById(Long id) {
        try {
            District district = districtRepository.findById(id).orElse(null);
            if (district == null) {
                return new ResponseEntity("District not found", HttpStatus.NOT_FOUND.value(), null);
            }
            return new ResponseEntity("District found", HttpStatus.OK.value(), mapToResponse(district));
        } catch (Exception e) {
            return new ResponseEntity("Error fetching District: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public ResponseEntity getAllDistricts() {
        try {
            List<District> districtList = districtRepository.findAll();
            List<DistrictResponse> responseList = districtList.stream().map(this::mapToResponse).toList();
            return new ResponseEntity("All Districts fetched successfully", HttpStatus.OK.value(), responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching Districts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    public ResponseEntity getDistrictsByState(Long stateId) {
        try {
            List<District> districtList = districtRepository.findByStateId(stateId);
            List<DistrictResponse> responseList = districtList.stream().map(this::mapToResponse).toList();
            return new ResponseEntity("Districts fetched successfully", HttpStatus.OK.value(), responseList);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching Districts: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity deleteDistrict(Long id) {
        try {
            District district = districtRepository.findById(id).orElse(null);
            if (district == null) {
                return new ResponseEntity("District not found", HttpStatus.NOT_FOUND.value(), null);
            }

            districtRepository.delete(district);
            return new ResponseEntity("District deleted successfully", HttpStatus.OK.value(), null);
        } catch (Exception e) {
            return new ResponseEntity("Error deleting District: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity uploadDistrictExcel(MultipartFile file, Long stateId) {
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("File is empty", HttpStatus.BAD_REQUEST.value(), null);
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return new ResponseEntity("No sheet found in file", HttpStatus.BAD_REQUEST.value(), null);
            }

            State state = stateRepository.findById(stateId).orElse(null);
            if (state == null) {
                return new ResponseEntity("State not found", HttpStatus.NOT_FOUND.value(), null);
            }

            Set<String> existingNames = districtRepository.findByStateId(stateId).stream()
                    .map(d -> d.getName().trim().toLowerCase()).collect(Collectors.toSet());

            List<District> newDistricts = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell cell = row.getCell(1);
                if (cell == null || cell.getCellType() != CellType.STRING) continue;

                String name = cell.getStringCellValue().trim();
                if (name.isEmpty()) continue;

                String lowerName = name.toLowerCase();
                if (!existingNames.contains(lowerName)) {
                    District district = new District();
                    district.setName(name);
                    district.setState(state);
                    newDistricts.add(district);
                    existingNames.add(lowerName);
                }
            }

            if (!newDistricts.isEmpty()) {
                districtRepository.saveAll(newDistricts);
            }

            return new ResponseEntity("District data uploaded successfully", HttpStatus.OK.value(),
                    newDistricts.size() + " rows processed");

        } catch (Exception e) {
            return new ResponseEntity("Error processing Excel file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
}
