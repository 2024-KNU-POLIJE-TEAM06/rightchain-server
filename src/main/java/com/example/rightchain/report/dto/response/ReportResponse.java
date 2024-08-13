package com.example.rightchain.report.dto.response;

import com.example.rightchain.chain.entity.Chain;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.entity.ReportType;

import java.util.List;
import java.util.stream.Collectors;

public record ReportResponse(
        Long reportId,
        String title,
        String content,
        ReportType reportType,
        boolean isCaseClose,
        String accountName,
        List<FileMetadata> files,
        List<ReportReadInChain> chains
) {

    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getReportId(),
                report.getTitle(),
                report.getContent(),
                report.getReportType(),
                report.isCaseClose(),
                report.getAccount().getName(),
                report.getFiles(),
                report.getChains().stream()
                        .map(chain -> ReportReadInChain.builder()
                                .walletName(chain.getWalletName())
                                .progressStatus(chain.getProgressStatus())
                                .address(chain.getAddress())
                                .build())
                        .collect(Collectors.toList())
        );
    }

}
