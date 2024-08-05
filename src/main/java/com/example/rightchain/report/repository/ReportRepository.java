package com.example.rightchain.report.repository;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.like.entity.Like;
import com.example.rightchain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
