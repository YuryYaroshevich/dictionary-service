package com.yra.dictionary.config.security;

import static com.yra.dictionary.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.yra.dictionary.config.security.SecurityConstants.HEADER_STRING;
import static com.yra.dictionary.config.security.SecurityConstants.SECRET;
import static com.yra.dictionary.config.security.SecurityConstants.TOKEN_PREFIX;

import com.yra.dictionary.model.Account;
import com.yra.dictionary.service.AccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;



public class GoogleOAuth2Filter extends AbstractAuthenticationProcessingFilter {
    private static final String EMAIL = "email";

    @Value(value = "${gmail.resource.userInfoUri}")
    private String userInfoUri;

    public GoogleOAuth2Filter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Autowired
    private AccountService userService;

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;

    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;


    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException,
            IOException, ServletException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        logger.debug(parameterMap.toString());
        /*if (oauth2RestTemplate.getAccessToken() != null) {
            throw new UserRedirectRequiredException(userInfoUri, new HashMap<>());
        }*/
        ResponseEntity<Object> forEntity = oauth2RestTemplate
                .getForEntity(userInfoUri, Object.class);



        @SuppressWarnings("unchecked")
        Map<String, String> profile = (Map<String, String>) forEntity.getBody();

        CustomOAuth2AuthenticationToken authenticationToken = getOAuth2Token(profile.get(EMAIL));
        authenticationToken.setAuthenticated(false);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private CustomOAuth2AuthenticationToken getOAuth2Token(String email) {
        UserDetails user;
        try {
            user = userService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            Account account = new Account();
            account.setEmail(email);
            account.setPassword("not_required");
            userService.createAccount(account);
            user = new User(account.getEmail(), account.getPassword(), Collections.emptyList());
        }

        CustomOAuth2AuthenticationToken authenticationToken =
                new CustomOAuth2AuthenticationToken(user);

        return authenticationToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        String email = ((User) auth.getPrincipal()).getUsername();
        res.sendRedirect("http://localhost:3000/dictionary?user=" + email);
    }

}
