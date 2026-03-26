package com.matrimony.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {

    private static final String EMAIL_REGEX = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return PATTERN.matcher(email).matches();
    }

    public boolean isCorporateEmail(String email) {
        if (!isValidEmail(email)) {
            return false;
        }
        
        // List of common free email providers
        String[] freeEmailDomains = {
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", 
            "aol.com", "icloud.com", "protonmail.com", "zoho.com"
        };
        
        String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
        
        for (String freeDomain : freeEmailDomains) {
            if (domain.equals(freeDomain)) {
                return false;
            }
        }
        
        return true;
    }
}