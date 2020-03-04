package com.chen.myproject.controller;

import com.chen.myproject.dto.AccessTokenDTO;
import com.chen.myproject.dto.GitHubUser;
import com.chen.myproject.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider provider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("{github.client.secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setCode(code);

        String token = provider.getAccessToken(accessTokenDTO);
        GitHubUser user = provider.getUser(token);

        if (user.getName()==null){
            user.setName("你没有名字");
        }

        if (user!=null){
            //登录成功，写cookie，session
            request.getSession().setAttribute("user",user);
            return "redirect:/";   //重定向是访问地址
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
