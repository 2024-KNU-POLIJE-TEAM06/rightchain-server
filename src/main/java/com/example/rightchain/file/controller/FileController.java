package com.example.rightchain.file.controller;

import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.file.service.FileService;
import com.example.rightchain.file.service.FileValidationServiceImpl;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileService fileService;
    private final FileValidationServiceImpl fileValidationService;

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Cannot upload empty file.");
        }

        if (!fileValidationService.validateFileExtension(file)) {
            return ResponseEntity.badRequest().body("Invalid file extension. Allowed extensions are: png, jpg, jpeg, pdf");
        }

        try {
            FileMetadata fileMetadata = fileService.saveFile(file, uploadDir);
            return ResponseEntity.ok("File '" + file.getOriginalFilename() + "' uploaded successfully as" + fileMetadata.getFileName());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload file due to IO error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while uploading the file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                    .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

            Path filePath = Paths.get(fileMetadata.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getOriginalFileName() + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
