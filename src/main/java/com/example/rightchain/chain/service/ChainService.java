package com.example.rightchain.chain.service;

import com.example.rightchain.chain.entity.Chain;
import com.example.rightchain.chain.entity.ProgressStatus;
import com.example.rightchain.chain.repository.ChainRepository;
import com.example.rightchain.report.entity.Report;
import com.example.rightchain.report.repository.ReportRepository;
import com.example.rightchain.wallet.api.BlockSDKApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChainService {
    private final ChainRepository chainRepository;
    private final BlockSDKApi blockSDKApi;
    private final ReportRepository reportRepository;

    private final String ENTRY_POINT = "CASE_CLOSED";

    @Transactional
    public void createChain(Report report) {
        String address = blockSDKApi.createWallet("[1] REPORT_SUBMITTED");

        Chain chain = chainRepository.save(
                Chain.builder()
                        .address(address)
                        .walletName("[1] REPORT_SUBMITTED")
                        .progressStatus(ProgressStatus.REPORT_SUBMITTED)
                        .report(report)
                        .build());

        report.getChains().add(chain);
    }

    @Transactional
    public void stackChain(Long reportId, String walletName) {
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new RuntimeException("report not found"));
        ProgressStatus currentProgressStatus = getCurrentProgress(report);
        if (walletName.equals(ENTRY_POINT)) {
            updateProgressStatus(report, currentProgressStatus);
        } else {
            String address = blockSDKApi.createWallet(walletName);

            chainRepository.save(
                    Chain.builder()
                            .address(address)
                            .walletName(walletName)
                            .progressStatus(currentProgressStatus)
                            .report(report)
                            .build());
        }

    }

    @Transactional
    public void updateProgressStatus(Report report, ProgressStatus currentProgressStatus) {
        if (currentProgressStatus.ordinal() >= ProgressStatus.values().length) {
            throw new RuntimeException("last progress status is " + currentProgressStatus.ordinal());
        }

        ProgressStatus updatedProgressStatus = ProgressStatus.values()[currentProgressStatus.ordinal() + 1];

        String walletName = ProgressStatus.getProgressStatusDescription(updatedProgressStatus);

        String address = blockSDKApi.createWallet(walletName);
        chainRepository.save(
                Chain.builder()
                        .address(address)
                        .walletName(walletName)
                        .progressStatus(updatedProgressStatus)
                        .report(report)
                        .build());
    }

    private ProgressStatus getCurrentProgress(Report report) {
        List<Chain> chains = findAllByReport(report);
        return chains.get(chains.size()-1).getProgressStatus();
    }

    public List<Chain> findAllByReport(Report report) {
        return chainRepository.findAllByReport(report);
    }
}
