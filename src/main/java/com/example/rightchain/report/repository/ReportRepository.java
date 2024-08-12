package com.example.rightchain.report.repository;


import com.example.rightchain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByAccountAccountId(Long accountId, Pageable pageable);
    Page<Report> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    @Query("SELECT r FROM Report r LEFT JOIN r.likes l GROUP BY r ORDER BY COUNT(l) DESC, r.reportId DESC")
    Page<Report> findAllOrderByLikesDesc(Pageable pageable);
}
