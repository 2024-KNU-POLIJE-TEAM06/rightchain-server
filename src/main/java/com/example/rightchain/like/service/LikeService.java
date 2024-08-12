package com.example.rightchain.like.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.like.entity.Like;
import com.example.rightchain.like.repository.LikeRepository;
import com.example.rightchain.report.dto.response.ReportResponse;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public Boolean toggleLike(Account account,Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()-> new RuntimeException("report not found"));
        if (likeRepository.existsByAccountAndReport(account,report)) {
            likeRepository.deleteByAccountAndReport(account,report);
            return false;
        }
        Like like = Like.builder()
                .account(account)
                .report(report)
                .build();

        likeRepository.save(like);

        return true;
    }

    public Long countLike(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(()-> new RuntimeException("report not found"));

        return likeRepository.countByReport(report);
    }

}
