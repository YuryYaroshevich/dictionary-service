package com.yra.dictionary.service;

import static com.yra.dictionary.config.security.SecurityConstants.TOKEN_PREFIX;
import static java.util.stream.Collectors.joining;

import com.yra.dictionary.dao.DictionaryDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareDictionaryService {
  private static final String SHARE_SECRET = "shareWithBatman";

  @Autowired
  DictionaryDao dictionaryDao;

  public String getSharedDictionariesToken(List<String> ids, String user) {
    if (dictionaryDao.exists(ids, user)) {
      String commaSeparatedIds = ids.stream().collect(joining(","));
      String tokenSubject = commaSeparatedIds + ":" + user;
      String token = Jwts.builder()
              .setSubject(tokenSubject)
              .signWith(SignatureAlgorithm.HS512, SHARE_SECRET)
              .compact();
      return token;
    }
    throw new RuntimeException("Some of dictionaries don't exist.");
  }

  public void receiveSharedDictionaries(String token, String user) {
    Claims claims = Jwts.parser()
            .setSigningKey(SHARE_SECRET)
            .parseClaimsJws(token)
            .getBody();
    String dictionaryIdsAndUser = claims.getSubject();
    String[] dictionaryIds = dictionaryIdsAndUser.split(":")[0].split(",");
    String userProvidedShare = dictionaryIdsAndUser.split(":")[1];
    dictionaryDao.copyDictionaries(
            Arrays.asList(dictionaryIds), userProvidedShare, user);
  }
}
