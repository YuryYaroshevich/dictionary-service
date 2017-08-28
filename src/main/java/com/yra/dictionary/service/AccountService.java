package com.yra.dictionary.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yra.dictionary.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  @Autowired
  private MongoCollection<Account> accountCollection;

  public Account createAccount(Account account) {
    accountCollection.insertOne(account);
    return account;
  }

  public Account findByEmail(String email) {
    return accountCollection.find(Filters.eq("email", email)).first();
  }
}
