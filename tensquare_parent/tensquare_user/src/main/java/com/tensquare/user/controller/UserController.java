package com.tensquare.user.controller;

import com.tensquare.entity.Result;
import com.tensquare.entity.StatusCode;
import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result userLogin(User user) {
        User result = userService.userLogin(user);
        if (result != null) {

            return new Result(true, StatusCode.OK, "登录成功");
        } else {
            return new Result(false, StatusCode.OK, "登录失败");
        }
    }
    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String userId){
        User user = userService.findById(userId);
        return new Result(true,StatusCode.OK,"查询成功",user);

    }

}
