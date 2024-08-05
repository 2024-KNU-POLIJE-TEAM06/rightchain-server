package com.example.rightchain.report.entity;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.file.entity.FileMetadata;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public Report(String title, String content, ReportType reportType, List<FileMetadata> files, Account account) {
        this.title = title;
        this.content = content;
        this.reportType = reportType;
        this.files = files;
        this.account = account;
        this.isCaseClose = false;
    }
}

