package com.example.rightchain.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class FileValidationServiceImpl implements FileValidationService {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "pdf");

    @Override
    public boolean validateFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}
