package com.example.rightchain.chain.dto.response;

import com.example.rightchain.chain.entity.Chain;
import com.example.rightchain.chain.entity.ProgressStatus;

import java.util.List;

public record ChainResponse(
        Long chainId,
        String address,
        String walletName,
        ProgressStatus progressStatus
        ) {
    public static ChainResponse from(Chain chain) {
        return new ChainResponse(
                chain.getChainId(),
                chain.getAddress(),
                chain.getWalletName(),
                chain.getProgressStatus()
        );
    }
}
