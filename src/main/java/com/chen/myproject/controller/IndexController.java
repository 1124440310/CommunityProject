package com.chen.myproject.controller;

import com.chen.myproject.mapper.UserMapper;
import com.chen.myproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    @Autowired
    private UserMapper mapper;

    @GetMapping("/")
    public String hello(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        User user=null;
        for (Cookie cookie : cookies){
            String name = cookie.getName();
            if ("token".equals(name)){
                String token=cookie.getValue();
                user= mapper.findByToken(token);
                if (user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }


        return "index";
    }
}
