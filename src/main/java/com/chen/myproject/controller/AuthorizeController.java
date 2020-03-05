package com.chen.myproject.controller;

import com.chen.myproject.dto.AccessTokenDTO;
import com.chen.myproject.dto.GitHubUser;
import com.chen.myproject.mapper.UserMapper;
import com.chen.myproject.model.User;
import com.chen.myproject.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider provider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam("code")String code,
                           @RequestParam("state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setCode(code);

        String accessToken = provider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = provider.getUser(accessToken);

        if (gitHubUser!=null){
            User user = new User();

            String token = UUID.randomUUID().toString();

            user.setToken(token);
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            userMapper.insert(user);

            //登录成功，写cookie，session

            response.addCookie(new Cookie("token",token));

            return "redirect:/";   //重定向是访问地址
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
