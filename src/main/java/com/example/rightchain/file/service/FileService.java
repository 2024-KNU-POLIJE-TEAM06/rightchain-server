package com.example.rightchain.file.service;

import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.report.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    private final FileMetadataRepository fileMetadataRepository;

    @Transactional
    public Long saveFile(MultipartFile file, String uploadDir) throws IOException {
        String fileName = UUID.randomUUID().toString();
        Path destinationPath = Paths.get(uploadDir).resolve(fileName);

        Files.copy(file.getInputStream(), destinationPath);

        FileMetadata fileMetadata = FileMetadata.builder()
                .originalFileName(file.getOriginalFilename())
                .fileName(fileName)
                .filePath(destinationPath.toString())
                .build();

        return fileMetadataRepository.save(fileMetadata).getFileId();
    }


}
