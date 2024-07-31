package com.example.rightchain.oauth.details;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.oauth.dto.GoogleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Account account;
    private final GoogleResponse googleResponse;

    //if you want to expand other site, you can refactor this.
    //this dto only used for google oauth
    @Override
    public Map<String, Object> getAttributes() {
        return googleResponse.attribute();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return account.getRole().name();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return account.getName();
    }
}
