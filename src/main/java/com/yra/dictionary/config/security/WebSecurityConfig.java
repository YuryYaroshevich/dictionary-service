package com.yra.dictionary.config.security;

import com.google.common.collect.ImmutableList;
import javax.annotation.Resource;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private UserDetailsService userDetailsService;

  @Autowired

  OAuth2ClientContext oauth2ClientContext;

  @Resource
  @Qualifier("accessTokenRequest")
  private AccessTokenRequest accessTokenRequest;

  @Scope("session")
  public void setAccessTokenRequest(AccessTokenRequest accessTokenRequest) {
    this.accessTokenRequest = accessTokenRequest;
  }

  @Autowired
  private OAuth2ClientContextFilter oAuth2ClientContextFilter;

  public WebSecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
            //.antMatchers("/login/**").permitAll()
            //.antMatchers("/login/gmail").anonymous()
            .anyRequest().authenticated()
            .and()
            .logout().logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)))
            //.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
            //.logoutSuccessUrl("http://localhost:3000")
            //.logoutUrl("/logout")
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(),
                    userDetailsService))
            /*.addFilterBefore(oAuth2ClientContextFilter, JWTAuthorizationFilter.class)
            .addFilterAfter(googleOAuth2Filter(), OAuth2ClientContextFilter.class)
            .userDetailsService(userDetailsService)*/;
            //.addFilterBefore(ssoFilter(), JWTAuthorizationFilter.class);
  }

  @Bean
  @ConfigurationProperties("gmail.client")
  public OAuth2ProtectedResourceDetails auth2ProtectedResourceDetails() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  public OAuth2RestTemplate oauth2RestTemplate() {
    return new OAuth2RestTemplate(auth2ProtectedResourceDetails(),
            new BatmanOAuth2ClientContext(accessTokenRequest));
  }


  @Bean
  public GoogleOAuth2Filter googleOAuth2Filter() {
    return new GoogleOAuth2Filter("/login/gmail");
  }

  /*
  *  Building our custom Google Provider
  * */
  @Bean
  public GoogleOauth2AuthProvider googleOauth2AuthProvider() {
    return new GoogleOauth2AuthProvider();
  }

  /*
  *  Using autowired to assign it to the auth manager
  * */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(googleOauth2AuthProvider());
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
    corsConfiguration.addAllowedOrigin("http://localhost:3000");
    corsConfiguration.addAllowedOrigin("https://accounts.google.com");
    corsConfiguration.setAllowedMethods(
            ImmutableList.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.addExposedHeader("Authorization");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private Filter ssoFilter() {
    OAuth2ClientAuthenticationProcessingFilter gmailFilter = new OAuth2ClientAuthenticationProcessingFilter(
            "/login/gmail");
    OAuth2RestTemplate gmailTemplate = new OAuth2RestTemplate(gmail(), oauth2ClientContext);
    gmailFilter.setRestTemplate(gmailTemplate);
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(gmailResource().getUserInfoUri(), gmail().getClientId());
    tokenServices.setRestTemplate(gmailTemplate);
    gmailFilter.setTokenServices(tokenServices);

    return gmailFilter;
  }

  @Bean
  @ConfigurationProperties("gmail.resource")
  public ResourceServerProperties gmailResource() {
    return new ResourceServerProperties();
  }

  @Bean
  @ConfigurationProperties("gmail.client")
  public AuthorizationCodeResourceDetails gmail() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(
          OAuth2ClientContextFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }
}


/*private Filter ssoFilter() {
    OAuth2ClientAuthenticationProcessingFilter gmailFilter = new OAuth2ClientAuthenticationProcessingFilter(
            "/login/gmail");
    OAuth2RestTemplate gmailTemplate = new OAuth2RestTemplate(gmail(), oauth2ClientContext);
    gmailFilter.setRestTemplate(gmailTemplate);
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(gmailResource().getUserInfoUri(), gmail().getClientId());
    tokenServices.setRestTemplate(gmailTemplate);
    gmailFilter.setTokenServices(tokenServices);

    return gmailFilter;
  }

  @Bean
  @ConfigurationProperties("gmail.client")
  public AuthorizationCodeResourceDetails gmail() {
    return new AuthorizationCodeResourceDetails();
  }

  @Bean
  @ConfigurationProperties("gmail.resource")
  public ResourceServerProperties gmailResource() {
    return new ResourceServerProperties();
  }

  @Bean
  public FilterRegistrationBean oauth2ClientFilterRegistration(
          OAuth2ClientContextFilter filter) {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(filter);
    registration.setOrder(-100);
    return registration;
  }*/