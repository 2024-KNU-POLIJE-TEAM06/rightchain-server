package com.example.rightchain.chain.entity;

import com.example.rightchain.report.entity.Report;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chainId;

    private String walletName;
    private String address;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Report.class)
    @JoinColumn(name = "report_id")
    private Report report; // 사건 번호


}
