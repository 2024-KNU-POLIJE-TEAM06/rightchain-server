package com.example.rightchain.oauth.dto;

import lombok.Getter;

import java.util.Map;

public record GoogleResponse(Map<String, Object> attribute) {

    public String getProvider() {
        return "google";
    }

    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    public String getEmail() {
        return attribute.get("email").toString();
    }

    public String getName() {
        return attribute.get("name").toString();
    }

}
