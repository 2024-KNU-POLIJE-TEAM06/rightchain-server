package com.example.rightchain.like.controller;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.like.service.LikeService;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{reportId}")
    @PreAuthorize("hasRole('USER') and isAuthenticated()")
    public ResponseEntity<Long> likeReport(
            Authentication authentication,
            @PathVariable Long reportId ) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Account account = oAuth2User.getAccount();
        Boolean liked = likeService.toggleLike(account, reportId);
        Long likesCount = likeService.countLike(reportId);

        if (liked) { // do likes
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(likesCount);
        } else { //likes removed
            return ResponseEntity.ok(likesCount);
        }
    }
}
