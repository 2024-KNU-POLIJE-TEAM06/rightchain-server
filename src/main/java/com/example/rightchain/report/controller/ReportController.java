package com.example.rightchain.report.controller;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import com.example.rightchain.report.dto.request.CreateReportRequest;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.service.ReportService;
import com.example.rightchain.wallet.component.BlockSDKApi;
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
    private final BlockSDKApi blockSDKApi;

    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReportById(@PathVariable Long reportId) {
        return null;
    }


    @PostMapping
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<String> writeReport(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User, //이거 테스트해보기
            @RequestBody CreateReportRequest createReportRequest) {

        Account account = customOAuth2User.getAccount();
        //create wallet
        String walletAddress = blockSDKApi.createWallet(createReportRequest.title());

        Report report = reportService.writeReport(createReportRequest, account, walletAddress);


        return ResponseEntity.status(HttpStatus.CREATED).body(walletAddress);
    }


}
