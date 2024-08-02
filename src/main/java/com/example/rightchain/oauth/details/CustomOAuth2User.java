package com.example.rightchain.oauth.details;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.oauth.dto.GoogleResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//if you want to expand to other site, you can refactor this.
//this only used for google oauth

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Account account;
    private GoogleResponse googleResponse;

    public CustomOAuth2User(Account account, GoogleResponse googleResponse) {
        this.account = account;
        this.googleResponse = googleResponse;
    }

    public CustomOAuth2User(Account account) {
        this.account = account;
    }

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
