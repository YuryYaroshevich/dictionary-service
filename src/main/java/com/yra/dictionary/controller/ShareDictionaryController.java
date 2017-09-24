package com.yra.dictionary.controller;

import com.yra.dictionary.service.ShareDictionaryService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dictionary")
public class ShareDictionaryController {
  @Autowired
  ShareDictionaryService shareDictionaryService;

  @PostMapping("share")
  ResponseEntity<SharedDictionaryToken> shareDictionaries(@RequestParam List<String> ids,
                                          Principal principal) {
    final String token = shareDictionaryService
            .getSharedDictionariesToken(ids, principal.getName());
    return new ResponseEntity(new SharedDictionaryToken(token), HttpStatus.OK);
  }

  @PostMapping("receive-shared")
  ResponseEntity receiveSharedDictionaries(@RequestParam String token,
                                                         Principal principal) {
    shareDictionaryService.receiveSharedDictionaries(token, principal.getName());
    return new ResponseEntity(HttpStatus.OK);
  }

  private static class SharedDictionaryToken {
    private String token;

    public SharedDictionaryToken(String token) {
      this.token = token;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }
  }
}
