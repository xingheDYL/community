package com.dyl.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyl.community.entity.User;
import com.dyl.community.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
public class UserService /*extends IService<User>*/ {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
