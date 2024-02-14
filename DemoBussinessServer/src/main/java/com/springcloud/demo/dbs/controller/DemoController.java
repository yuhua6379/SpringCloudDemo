package com.springcloud.demo.dbs.controller;

import com.springcloud.demo.dbs.dto.UserDto;
import com.springcloud.demo.dbs.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "Demo")
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    AuthService authService;

    @GetMapping(value = "/hello")
    @ApiOperation(value = "hello接口", notes = "hello不能直接被调用，必须先获取token登录，否则无法直接调用")
    @ResponseBody
    public String hello(@Parameter(hidden = true) @RequestHeader(value = "X-UserName", required = false) String userName) {

        UserDto user = null;
        if (userName != null && !userName.isEmpty()) {
            user = authService.info(userName);

        }

        if (user == null || user.getUserName() == null || user.getUserName().isEmpty()) {
            return "I apologize for having some errors when fetching your account info.";
        }

        return "Hello, your username is " + user.getUserName() + ". This account is created at " + user.getCreateTime();
    }

}
