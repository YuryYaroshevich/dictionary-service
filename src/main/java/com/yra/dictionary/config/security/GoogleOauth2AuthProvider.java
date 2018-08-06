package com.yra.dictionary.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class GoogleOauth2AuthProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomOAuth2AuthenticationToken token = (CustomOAuth2AuthenticationToken) authentication;
        UserDetails registeredUser = (UserDetails) token.getPrincipal();
        userDetailsService.loadUserByUsername(registeredUser.getUsername());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomOAuth2AuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
