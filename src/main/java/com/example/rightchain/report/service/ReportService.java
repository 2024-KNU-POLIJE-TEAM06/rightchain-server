package com.example.rightchain.report.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.account.repository.AccountRepository;
import com.example.rightchain.exception.ReportNotFoundException;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.file.service.FileService;
import com.example.rightchain.like.repository.LikeRepository;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.dto.response.ReportResponse;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final LikeRepository likeRepository;
    private final FileService fileService;

    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    @Transactional
    public Report writeReport(CreateReportRequest createReportRequest, Account account) throws IOException {

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
            fileService.saveFile(file, uploadDir, report);
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
