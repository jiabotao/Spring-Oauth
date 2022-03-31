package org.example.authserver.controller;

import org.example.authserver.api.CommonResult;
import org.example.authserver.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @PostMapping(value = "/token")
    public CommonResult<TokenDTO> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(oAuth2AccessToken.getValue());
        tokenDTO.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
        tokenDTO.setTokenHead("Bearer ");
        return CommonResult.success(tokenDTO);
    }
}
