package com.example.rightchain.oauth.handler;

import com.example.rightchain.oauth.dto.Token;
import com.example.rightchain.security.provider.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess 성공");
        Token token = jwtTokenProvider.generateToken(authentication);

        Cookie cookie = new Cookie("auth", token.accessToken());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 10);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.sendRedirect("http://ec2-108-136-47-162.ap-southeast-3.compute.amazonaws.com/:53000/");
    }

}
