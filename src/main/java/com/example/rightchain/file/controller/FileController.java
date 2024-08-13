package com.example.rightchain.file.controller;

import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.file.service.FileService;
import com.example.rightchain.file.service.FileValidationServiceImpl;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileMetadataRepository fileMetadataRepository;
    private final FileService fileService;
    private final FileValidationServiceImpl fileValidationService;

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            //return ResponseEntity.badRequest().body("Cannot upload empty file.");
            throw new RuntimeException("file is empty");
        }

        if (!fileValidationService.validateFileExtension(file)) {
            //return ResponseEntity.badRequest().body("Invalid file extension. Allowed extensions are: png, jpg, jpeg, pdf");
            throw new RuntimeException("file is not valid");
        }

        try {
            return ResponseEntity.ok(fileService.saveFile(file, uploadDir));
        } catch (IOException e) {
            throw new RuntimeException(e);
            //return ResponseEntity.badRequest().body("Failed to upload file due to IO error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
            //return ResponseEntity.internalServerError().body("An error occurred while uploading the file: " + e.getMessage());
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

            // 파일 이름을 UTF-8로 인코딩
            String encodedFileName = URLEncoder.encode(fileMetadata.getOriginalFileName(), StandardCharsets.UTF_8);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .body(resource);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
