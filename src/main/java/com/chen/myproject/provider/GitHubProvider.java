package com.chen.myproject.provider;

import com.alibaba.fastjson.JSON;
import com.chen.myproject.dto.AccessTokenDTO;
import com.chen.myproject.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {
    public String getAccessToken(AccessTokenDTO dto){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(dto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();

            String[] split = string.split("&");
            String[] result = split[0].split("=");

            return result[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        GitHubUser user = null;
        try {
            Response response = client.newCall(request).execute();
            String res =response.body().string();
            user = JSON.parseObject(res, GitHubUser.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
