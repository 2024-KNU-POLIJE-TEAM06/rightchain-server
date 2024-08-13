package com.example.rightchain.report.dto.request;


import com.example.rightchain.report.entity.ReportType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateReportRequest(
        String title,
        String content,
        ReportType reportType,
        List<Long> filesId
) {

}
