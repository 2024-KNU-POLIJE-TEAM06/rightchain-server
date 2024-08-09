package com.example.rightchain.wallet.controller;

import com.example.rightchain.wallet.component.BlockSDKApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final BlockSDKApi blockSDKApi;

    @GetMapping
    public ResponseEntity<Map> readWalletByAddress(
            @RequestParam("address") String address) {
        Map walletInfo = blockSDKApi.readWallet(address);
        if (walletInfo == null) {
            throw new IllegalStateException("failed to read wallet");
        }

        return ResponseEntity.ok(walletInfo);
    }
}
