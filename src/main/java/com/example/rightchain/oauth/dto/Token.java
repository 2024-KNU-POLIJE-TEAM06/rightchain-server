package com.example.rightchain.oauth.dto;

import lombok.Builder;

@Builder
public record Token(
        String grantType,
        String accessToken
) {
}
