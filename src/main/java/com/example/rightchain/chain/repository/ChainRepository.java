package com.example.rightchain.chain.repository;

import com.example.rightchain.chain.entity.Chain;
import com.example.rightchain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChainRepository extends JpaRepository<Chain, Long> {
    @Query("SELECT c FROM Chain c WHERE c.report.reportId = :reportId ORDER BY c.chainId DESC")
    Chain findTopByReportIdOrderByChainIdDesc(Long reportId);

    List<Chain> findAllByReport(Report report);
}
