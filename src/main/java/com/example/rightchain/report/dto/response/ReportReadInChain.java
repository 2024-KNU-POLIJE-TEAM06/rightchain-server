package com.example.rightchain.report.dto.response;


import com.example.rightchain.chain.entity.ProgressStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportReadInChain {
    private String walletName;
    private ProgressStatus progressStatus;
    private String address;

    @Builder
    public ReportReadInChain(String walletName, ProgressStatus progressStatus, String address) {
        this.walletName = walletName;
        this.progressStatus = progressStatus;
        this.address = address;
    }

}
