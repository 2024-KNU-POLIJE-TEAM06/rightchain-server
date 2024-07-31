package com.example.rightchain.oauth.service;

import com.example.rightchain.account.entity.Account;
import com.example.rightchain.account.entity.Role;
import com.example.rightchain.account.repository.AccountRepository;
import com.example.rightchain.oauth.details.CustomOAuth2User;
import com.example.rightchain.oauth.dto.GoogleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals("google")) {
            GoogleResponse googleResponse = new GoogleResponse(oAuth2User.getAttributes());
            String email = googleResponse.getEmail();
            Optional<Account> accountOptional = accountRepository.findByEmail(email);

            Account account;
            if (accountOptional.isPresent()) {
                account = accountOptional.get();
            }
            else {
                account = Account.builder()
                        .email(email)
                        .name(googleResponse.getName())
                        .role(Role.ROLE_USER)
                        .build();
                accountRepository.save(account);
            }
            return new CustomOAuth2User(account, googleResponse);
        }
        else {
            throw new OAuth2AuthenticationException("Unsupported registrationId: " + registrationId);
        }
    }
}
