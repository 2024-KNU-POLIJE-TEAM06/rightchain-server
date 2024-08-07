package com.example.rightchain.report.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final FileMetadataRepository fileMetadataRepository;

    @Transactional
    public Report writeReport(CreateReportRequest createReportRequest, Account account, String walletAddress) {
        List<FileMetadata> files = fileMetadataRepository.findAllById(createReportRequest.fileIds());

        Report report = Report.builder()
                .account(account)
                .files(files)
                .walletAddress(walletAddress)
                .reportType(createReportRequest.reportType())
                .content(createReportRequest.content())
                .title(createReportRequest.title())
                .build();

        return reportRepository.save(report);
    }


    public Report findById(Long reportId) {
        return reportRepository.findById(reportId).orElseThrow(()-> new IllegalStateException("Report not found"));
    }
}
