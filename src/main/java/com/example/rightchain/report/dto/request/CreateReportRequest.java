package com.example.rightchain.report.dto.request;


import com.example.rightchain.account.entity.Account;
import com.example.rightchain.file.entity.FileMetadata;
import com.example.rightchain.file.repository.FileMetadataRepository;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.entity.ReportType;
import java.util.List;

public record CreateReportRequest(
        String title,
        String content,
        ReportType reportType,
        List<Long> fileIds
) {

}
