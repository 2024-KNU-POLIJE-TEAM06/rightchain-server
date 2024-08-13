package com.example.rightchain.report.dto.response;

import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.entity.ReportType;

import java.util.List;

public record ReportResponse(
        Long reportId,
        String title,
        String content,
        ReportType reportType,
        boolean isCaseClose,
        String accountName,
        List<FileMetadata> files
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getReportId(),
                report.getTitle(),
                report.getContent(),
                report.getReportType(),
                report.isCaseClose(),
                report.getAccount().getName(),
                report.getFiles()
        );
    }
}
