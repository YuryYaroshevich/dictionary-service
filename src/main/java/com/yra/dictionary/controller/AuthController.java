package com.yra.dictionary.controller;

import com.yra.dictionary.model.Account;
import com.yra.dictionary.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  private AccountService accountService;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;


  @PostMapping("sign-up")
  public ResponseEntity signUp(@RequestBody Account account) {
    String encryptedPass = bCryptPasswordEncoder.encode(account.getPassword());
    account.setPassword(encryptedPass);
    accountService.createAccount(account);
    return new ResponseEntity(HttpStatus.CREATED);
  }
}
