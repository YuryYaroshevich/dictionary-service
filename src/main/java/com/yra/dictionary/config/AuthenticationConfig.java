package com.yra.dictionary.config;

import static java.lang.String.format;

import com.yra.dictionary.model.Account;
import com.yra.dictionary.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {
  @Autowired
  private AccountService accountService;

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService());
  }

  @Bean
  UserDetailsService userDetailsService() {
    return email -> {
      Account account = accountService.findByEmail(email);
      if(account != null) {
        return new User(account.getEmail(), account.getPassword(),
                AuthorityUtils.createAuthorityList("USER"));
      } else {
        throw new UsernameNotFoundException(format("Could not find user with email: '%'", email));
      }
    };
  }
}
