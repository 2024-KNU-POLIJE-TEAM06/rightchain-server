package com.example.rightchain.report.entity;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.base.BaseTimeEntity;
import com.example.rightchain.chain.entity.Chain;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.like.entity.Like;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    private boolean isCaseClose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private List<FileMetadata> files = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chain> chains = new ArrayList<>();


    @Builder
    public Report(String title, String content,
                  ReportType reportType,
                  List<FileMetadata> files,
                  Account account,
                  List<Chain> chains) {
        this.title = title;
        this.content = content;
        this.reportType = reportType;
        this.files = files;
        this.account = account;
        this.isCaseClose = false;
        this.chains = chains;
    }
}

