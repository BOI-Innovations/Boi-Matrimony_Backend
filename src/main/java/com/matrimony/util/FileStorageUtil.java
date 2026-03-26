package com.matrimony.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${matrimony.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${matrimony.max.file.size:10485760}") // 10MB default
    private long maxFileSize;

    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        return storeFile(file, subDirectory, null);
    }

    public String storeFile(MultipartFile file, String subDirectory, String customFileName) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IOException("File size exceeds maximum limit");
        }

        // Validate file type
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        
        if (!isAllowedFileType(fileExtension)) {
            throw new IOException("File type not allowed");
        }

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName = customFileName != null ? customFileName : 
                         UUID.randomUUID().toString() + fileExtension;
        
        Path filePath = uploadPath.resolve(fileName);
        
        // Copy file to target location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return subDirectory + "/" + fileName;
    }

    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir, filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + filePath);
        }
        return Files.readAllBytes(path);
    }

    public boolean fileExists(String filePath) {
        Path path = Paths.get(uploadDir, filePath);
        return Files.exists(path);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private boolean isAllowedFileType(String fileExtension) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"};
        for (String ext : allowedExtensions) {
            if (ext.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    public String getFileUrl(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        return "/uploads/" + filePath;
    }

    public long getFileSize(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath);
        if (!Files.exists(path)) {
            throw new IOException("File not found");
        }
        return Files.size(path);
    }
}