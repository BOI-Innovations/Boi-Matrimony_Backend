package com.matrimony.controller.api;

import com.matrimony.model.entity.ResponseEntity;
import com.matrimony.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/data")
    public ResponseEntity getDashboardData() {
        try {
            Map<String, Object> dashboardData = dashboardService.getDashboardData();
            return new ResponseEntity("Success", HttpStatus.OK.value(), dashboardData);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching dashboard data: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
    
    @GetMapping("/dateByDateRange")
    public ResponseEntity getDashboardData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        try {
            Map<String, Object> dashboardData = dashboardService.getDashboardData(fromDate, toDate);
            return new ResponseEntity("Success", HttpStatus.OK.value(), dashboardData);
        } catch (Exception e) {
            return new ResponseEntity("Error fetching dashboard data: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
    
    
}