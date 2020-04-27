package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User userLogin(User user) {
        return userDao.selectOne(user);

    }

    //    根据id查询用户
    public User findById(String userId) {
        return userDao.selectById(userId);
    }
}
