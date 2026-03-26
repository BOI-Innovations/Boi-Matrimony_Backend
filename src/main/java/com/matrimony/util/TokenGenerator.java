package com.matrimony.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Component
public class TokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public String generateVerificationToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public String generateEmailVerificationToken() {
        return "email_verification_" + UUID.randomUUID().toString();
    }

    public String generatePasswordResetToken() {
        return "password_reset_" + UUID.randomUUID().toString();
    }

    public String generateApiKey() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return "api_key_" + base64Encoder.encodeToString(randomBytes);
    }

    public String generateSessionToken() {
        return "session_" + UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
    }

    public String generateShortCode(int length) {
        if (length < 4) length = 4;
        if (length > 8) length = 8;

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }

        return code.toString();
    }

    public String generateReferenceNumber() {
        return "REF-" + LocalDateTime.now().getYear() + 
               String.format("%02d", LocalDateTime.now().getMonthValue()) +
               String.format("%02d", LocalDateTime.now().getDayOfMonth()) +
               "-" + generateShortCode(6);
    }

    public boolean isValidTokenFormat(String token, String type) {
        if (token == null) return false;

        switch (type.toLowerCase()) {
            case "email_verification":
                return token.startsWith("email_verification_");
            case "password_reset":
                return token.startsWith("password_reset_");
            case "api_key":
                return token.startsWith("api_key_");
            case "session":
                return token.startsWith("session_");
            default:
                return token.length() >= 32; // Generic validation
        }
    }

    public String generateSecureRandomString(int length) {
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}