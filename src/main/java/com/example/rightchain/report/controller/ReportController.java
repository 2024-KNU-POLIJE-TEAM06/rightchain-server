package com.example.rightchain.report.controller;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.dto.response.ReportResponse;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long reportId) {
        ReportResponse response = reportService.getReportById(reportId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<String> writeReport(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User, //이거 테스트해보기
            @RequestBody CreateReportRequest createReportRequest) {

        Account account = customOAuth2User.getAccount();

        Report report = reportService.writeReport(createReportRequest, account);


        return ResponseEntity.status(HttpStatus.CREATED).body(report.getTitle());
    }



}