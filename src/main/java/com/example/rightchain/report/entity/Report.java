package com.example.rightchain.report.entity;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.file.entity.FileMetadata;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String title;
    private String content;
    private ReportType reportType;
    private boolean isCaseClose;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private List<FileMetadata> files = new ArrayList<>();

    //이부분 파일 추가하는거 dto로 받아서 저장 argument dto로 바꾸기
//    public void addFile(String fileName, String filePath) {
//        FileMetadata fileMetadata = new FileMetadata(fileName, filePath);
//        this.files.add}
}

