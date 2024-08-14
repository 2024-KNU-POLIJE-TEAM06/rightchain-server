package com.example.rightchain.chain.controller;

import com.example.rightchain.chain.dto.response.ChainResponse;
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
    public ResponseEntity<ChainResponse> stackChain(
            @PathVariable("reportId") Long reportId) {

        return ResponseEntity.ok(chainService.stackChain(reportId));
    }
}
