package com.yra.dictionary.service;

import static java.util.Collections.emptyList;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
  @Autowired
  private MongoCollection<Account> accountCollection;


  public Account createAccount(Account account) {
    accountCollection.insertOne(account);
    return account;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    Account account = accountCollection.find(Filters.eq("email", email)).first();
    if (account == null) {
      throw new UsernameNotFoundException(email);
    }
    return new User(account.getEmail(), account.getPassword(), emptyList());
  }
}
