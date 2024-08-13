package com.example.rightchain.account.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;

    public boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    public Account findByEmail(String email) {
        log.info("email= {}", email);
        return accountRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Account not found"));
    }
}
