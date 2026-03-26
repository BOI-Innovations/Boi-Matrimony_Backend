package com.matrimony.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PhoneValidator {

    private static final String PHONE_REGEX = 
        "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
    
    private static final Pattern PATTERN = Pattern.compile(PHONE_REGEX);

    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove any spaces, dashes, or parentheses
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        return PATTERN.matcher(cleanedNumber).matches();
    }

    public String formatPhoneNumber(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) {
            return phoneNumber;
        }
        
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        
        if (cleanedNumber.startsWith("+")) {
            // International format: +91 1234567890
            String countryCode = cleanedNumber.substring(0, cleanedNumber.length() - 10);
            String number = cleanedNumber.substring(cleanedNumber.length() - 10);
            return countryCode + " " + number;
        } else {
            // Indian format: 12345 67890
            if (cleanedNumber.length() == 10) {
                return cleanedNumber.substring(0, 5) + " " + cleanedNumber.substring(5);
            }
        }
        
        return phoneNumber;
    }

    public String extractCountryCode(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.startsWith("+")) {
            return null;
        }
        
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        if (cleanedNumber.length() > 10) {
            return cleanedNumber.substring(0, cleanedNumber.length() - 10);
        }
        
        return null;
    }
}