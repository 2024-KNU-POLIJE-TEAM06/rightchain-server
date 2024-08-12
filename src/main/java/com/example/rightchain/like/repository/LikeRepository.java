package com.example.rightchain.like.repository;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.like.entity.Like;
import com.example.rightchain.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {
    boolean existsByAccountAndReport(Account account, Report report);
    void deleteByAccountAndReport(Account account, Report report);
    Long countByReport(Report report);
    Page<Like> findByAccountAccountId(Long accountId, Pageable pageable);
}
