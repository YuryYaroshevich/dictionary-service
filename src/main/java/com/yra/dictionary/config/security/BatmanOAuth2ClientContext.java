package com.yra.dictionary.config.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;


public class BatmanOAuth2ClientContext extends DefaultOAuth2ClientContext {
  public BatmanOAuth2ClientContext(AccessTokenRequest accessTokenRequest) {
    super(accessTokenRequest);
  }

  /*@Override
  public AccessTokenRequest getAccessTokenRequest() {
    AccessTokenRequest tokenRequest = super.getAccessTokenRequest();
    if (tokenRequest != null) {
      String stateKey = tokenRequest.getStateKey();
      tokenRequest.setPreservedState(stateKey);
    }
    return new BatmanAccessTokenRequest(tokenRequest);
  }*/

  private static class BatmanAccessTokenRequest extends DefaultAccessTokenRequest {

    public BatmanAccessTokenRequest(AccessTokenRequest accessTokenRequest) {
      this(convertToMap(accessTokenRequest));
    }

    private static Map<String, String[]> convertToMap(AccessTokenRequest accessTokenRequest) {
      Map<String, String[]> parameters = new HashMap<>();

      accessTokenRequest.forEach((String key, List<String> values) -> {
        parameters.put(key, (String[])values.toArray());
      });
      return parameters;
    }

    public BatmanAccessTokenRequest(Map<String, String[]> parameters) {
      super(parameters);
    }

    @Override
    public Object getPreservedState() {
      return super.getStateKey();
    }
  }
}
