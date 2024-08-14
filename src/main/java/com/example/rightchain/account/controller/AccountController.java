package com.example.rightchain.account.controller;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getAccount(@AuthenticationPrincipal CustomOAuth2User customOAuth2User
            ) {
        if (customOAuth2User != null) {
            Account account = customOAuth2User.getAccount();
            return ResponseEntity.ok("User is logged in as: " + account.getEmail());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();

        // "auth" 쿠키 삭제 (JWT 토큰이 저장된 쿠키)
        Cookie cookie = new Cookie("auth", null);
        cookie.setPath("/"); // 쿠키의 경로 설정
        cookie.setHttpOnly(true); // HttpOnly 속성 설정
        cookie.setMaxAge(0); // 쿠키 즉시 삭제
        response.addCookie(cookie);

        return ResponseEntity.ok().body("logout");
    }
}
