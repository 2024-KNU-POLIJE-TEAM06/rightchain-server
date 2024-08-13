package com.example.rightchain.report.controller;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.file.service.FileValidationServiceImpl;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.dto.response.ReportResponse;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final FileValidationServiceImpl fileValidationService;

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long reportId) {
        ReportResponse response = reportService.getReportById(reportId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<String> writeReport(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User, //test this
            @RequestBody CreateReportRequest createReportRequest) throws IOException {

        Account account = customOAuth2User.getAccount();

        // 파일 확장자 유효성 검사
        for (MultipartFile file : createReportRequest.files()) {
            if (!fileValidationService.validateFileExtension(file)) {
                return ResponseEntity.badRequest().body("Invalid file extension. Allowed extensions are: png, jpg, jpeg, pdf");
            }
        }

        Report report = reportService.writeReport(createReportRequest, account);


        return ResponseEntity.status(HttpStatus.CREATED).body(report.getTitle());
    }


    @GetMapping("/my-reports")
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<Page<ReportResponse>> getMyReports(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Account account = customOAuth2User.getAccount();
        Pageable pageable =PageRequest.of(page,size);
        Page<ReportResponse> response = reportService.getMyReports(account,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my-likes")
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<Page<ReportResponse>> getMyLikedReports(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Account account = customOAuth2User.getAccount();
        Pageable pageable =PageRequest.of(page,size);
        Page<ReportResponse> response = reportService.getMyLikedReports(account,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //searchReport, only by title
    @GetMapping("/search")
    public ResponseEntity<Page<ReportResponse>> searchReport(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable =PageRequest.of(page,size);
        Page<ReportResponse> responses = reportService.searchReportsByTitle(keyword,pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/top-liked")
    public ResponseEntity<Page<ReportResponse>> getTopLikedReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReportResponse> response = reportService.getReportsOrderByLikes(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}