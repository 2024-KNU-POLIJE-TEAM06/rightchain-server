package com.example.rightchain.file.repository;

import com.example.rightchain.file.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata,Long> {
}
