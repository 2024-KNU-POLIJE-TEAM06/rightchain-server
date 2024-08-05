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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final FileMetadataRepository fileMetadataRepository;

    @Transactional
    public Report writeReport(CreateReportRequest createReportRequest, Account account) {
        List<FileMetadata> files = fileMetadataRepository.findAllById(createReportRequest.fileIds());

        Report report = Report.builder()
                .account(account)
                .files(files)
                .reportType(createReportRequest.reportType())
                .content(createReportRequest.content())
                .title(createReportRequest.title())
                .build();

        return reportRepository.save(report);
    }


}
