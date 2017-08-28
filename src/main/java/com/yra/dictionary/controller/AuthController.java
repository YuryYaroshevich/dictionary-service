package com.yra.dictionary.controller;

import com.yra.dictionary.model.Account;
import com.yra.dictionary.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthController {
  @Autowired
  private AccountService accountService;

  @PostMapping("signup")
  public Account signUp(@RequestBody Account account) {
    accountService.createAccount(account);
    return account;
  }
}
