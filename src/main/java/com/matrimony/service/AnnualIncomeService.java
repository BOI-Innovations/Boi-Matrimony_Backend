package com.matrimony.service;

import org.springframework.web.multipart.MultipartFile;
import com.matrimony.model.dto.request.AnnualIncomeRequest;
import com.matrimony.model.entity.ResponseEntity;

public interface AnnualIncomeService {

    ResponseEntity createOrUpdateAnnualIncome(AnnualIncomeRequest request);

    ResponseEntity getAnnualIncomeById(Long id);

    ResponseEntity getAllAnnualIncomes();

    ResponseEntity deleteAnnualIncome(Long id);

    ResponseEntity uploadAnnualIncomeExcel(MultipartFile file);
}
