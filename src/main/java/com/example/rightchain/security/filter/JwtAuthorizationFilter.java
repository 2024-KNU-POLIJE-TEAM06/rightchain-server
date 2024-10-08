package com.example.rightchain.security.filter;

import com.example.rightchain.account.service.AccountService;
import com.example.rightchain.security.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountService accountService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (isReissueRequest((HttpServletRequest) request)) {
            filterChain.doFilter(request, response);
            return ;
        }
        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        log.info("토큰 : "+token);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token, accountService);
            log.info("이프문 check");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    private boolean isReissueRequest(HttpServletRequest request) {
        return request.getServletPath().equals("/auth/reissue");
    }
}
