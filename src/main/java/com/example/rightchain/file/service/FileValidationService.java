package com.example.rightchain.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidationService {
    boolean validateFileExtension(MultipartFile file);
}
