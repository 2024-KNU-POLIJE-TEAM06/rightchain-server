package com.example.rightchain.chain.controller;

import com.example.rightchain.chain.dto.request.ChainStackRequest;
import com.example.rightchain.chain.service.ChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chains")
@RequiredArgsConstructor
public class ChainController {
    private final ChainService chainService;

    @PostMapping("/stacks/{reportId}")
    public ResponseEntity<String> stackChain(
            @PathVariable("reportId") Long reportId,
            @RequestBody ChainStackRequest chainStackRequest) {

        chainService.stackChain(reportId, chainStackRequest.walletName());

        return ResponseEntity.ok("Wallet named '" + chainStackRequest.walletName() + "' has been successfully stacked.");
    }
}
