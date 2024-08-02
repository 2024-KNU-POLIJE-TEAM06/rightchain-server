package com.example.rightchain.oauth.dto;

import java.util.Map;

public record GoogleResponse(Map<String, Object> attribute) {

    public String getProvider() {
        return "google";
    }

    public String getEmail() {
        return attribute.get("email").toString();
    }

    public String getName() {
        return attribute.get("name").toString();
    }

}
