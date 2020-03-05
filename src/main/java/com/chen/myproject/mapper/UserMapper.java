package com.chen.myproject.mapper;

import com.chen.myproject.model.User;
import org.apache.ibatis.annotations.Mapper;

public interface UserMapper {

    void insert(User user);
}
