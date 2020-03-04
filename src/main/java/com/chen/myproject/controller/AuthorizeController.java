package com.chen.myproject.controller;

import com.chen.myproject.dto.AccessTokenDTO;
import com.chen.myproject.dto.GitHubUser;
import com.chen.myproject.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider provider;

    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("ddaec34bf350e4f3d4fe");
        accessTokenDTO.setClient_secret("e8b747a42772e3a9c65b1611ca7a419f442b7e46");
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setCode(code);

        String token = provider.getAccessToken(accessTokenDTO);
        GitHubUser user = provider.getUser(token);
        System.out.println(user);

        return "index";
    }
}
