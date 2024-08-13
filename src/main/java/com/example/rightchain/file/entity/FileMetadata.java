package com.example.rightchain.file.entity;


import com.example.rightchain.report.entity.Report;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String originalFileName;
    private String fileName; //uuid
    private String filePath;

    @Builder
    public FileMetadata(String originalFileName,String fileName, String filePath) {
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }

}
