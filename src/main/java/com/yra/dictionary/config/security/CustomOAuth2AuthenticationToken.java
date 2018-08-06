package com.yra.dictionary.config.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomOAuth2AuthenticationToken extends AbstractAuthenticationToken {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8254831403638075928L;

    private UserDetails registeredUser;

    public CustomOAuth2AuthenticationToken(
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public CustomOAuth2AuthenticationToken(UserDetails registeredUser) {
        super(Collections.emptyList());
        this.registeredUser = registeredUser;
    }

    @Override
    public Object getCredentials() {
        return "NOT_REQUIRED";
    }

    @Override
    public Object getPrincipal() {
        return registeredUser;
    }

    /**
     * @return the registeredUser
     */
    public UserDetails getUserDetail() {
        return registeredUser;
    }

    /**
     * @param registeredUser
     *            the registeredUser to set
     */
    public void setUserDetail(UserDetails registeredUser) {
        this.registeredUser = registeredUser;
        setDetails(registeredUser);
    }
}
