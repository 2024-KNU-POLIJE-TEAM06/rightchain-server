package com.example.rightchain.report.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.exception.ReportNotFoundException;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.like.repository.LikeRepository;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.dto.response.ReportResponse;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Report writeReport(CreateReportRequest createReportRequest, Account account) {

        Report report = Report.builder()
                .account(account)
                .reportType(createReportRequest.reportType())
                .content(createReportRequest.content())
                .title(createReportRequest.title())
                .build();

        reportRepository.save(report);

        for (MultipartFile file : createReportRequest.files()) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Cannot upload empty file.");
            }
            String fileName = UUID.randomUUID().toString();
            Path destinationPath = Paths.get("path/to/your/storage").resolve(fileName);

            try {
                Files.copy(file.getInputStream(), destinationPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file", e);
            }

            FileMetadata fileMetadata = FileMetadata.builder()
                    .originalFileName(file.getOriginalFilename())
                    .fileName(fileName)
                    .filePath(destinationPath.toString())
                    .report(report) // 연관 관계 설정
                    .build();

            fileMetadataRepository.save(fileMetadata);
        }

        return report;
    }

    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("Report not found "+ reportId));

        return ReportResponse.from(report);
    }


    public Page<ReportResponse> getMyReports(Account account, Pageable pageable) {
        return reportRepository.findByAccountAccountId(account.getAccountId(), pageable)
                .map(ReportResponse::from);
    }

    public Page<ReportResponse> getMyLikedReports(Account account, Pageable pageable) {

        return likeRepository.findByAccountAccountId(account.getAccountId(), pageable)
                .map(like -> ReportResponse.from(like.getReport()));
    }

    public Page<ReportResponse> searchReportsByTitle(String keyword, Pageable pageable) {

        return reportRepository.findByTitleContainingIgnoreCase(keyword,pageable).map(ReportResponse::from);
    }

    public Page<ReportResponse> getReportsOrderByLikes(Pageable pageable) {
        return reportRepository.findAllOrderByLikesDesc(pageable).map(ReportResponse::from);
    }
}
