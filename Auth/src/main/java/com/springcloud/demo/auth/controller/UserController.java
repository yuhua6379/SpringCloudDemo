package com.springcloud.demo.auth.controller;

import com.springcloud.demo.auth.dto.TokenDto;
import com.springcloud.demo.auth.dto.UserDto;
import com.springcloud.demo.auth.dto.UserTokenApplicationDto;
import com.springcloud.demo.auth.dto.VerifyDto;
import com.springcloud.demo.auth.entity.User;
import com.springcloud.demo.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(tags = "User")
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/token")
    @ApiOperation(value = "生成token", notes = "如果没有token则所有接口都会401，必须先从此接口获取token，再携带token访问")
    @ResponseBody
    public ResponseEntity<TokenDto> token(@RequestBody UserTokenApplicationDto tokenApp) {

        User user = userService.verifyUserWithPassword(tokenApp.getUserName(), tokenApp.getPassWord());
        if (user == null) {
            return ResponseEntity.ok(TokenDto.fail());
        } else {
            return ResponseEntity.ok(TokenDto.success(userService.generateToken(user)));
        }
    }


    @GetMapping(value = "/verify")
    @ApiOperation(value = "校验token", notes = "校验token的合法性，成功后返回user的info，是一个特殊内部接口，一般不会使用，除了gateway以外")
    @ResponseBody
    public ResponseEntity<VerifyDto> verify(@RequestParam String token) {
        if (userService.verifyToken(token)){
            String userName = userService.parseUserNameFromToken(token);
            return ResponseEntity.ok(VerifyDto.success(userName));
        }else{
            return ResponseEntity.ok(VerifyDto.fail());
        }
    }


    @GetMapping(value = "/info")
    @ApiOperation(value = "获取user的info", notes = "这个接口需要提供UserName以获取user的info，是一个内部接口")
    @ResponseBody
    public ResponseEntity<UserDto> info(@RequestParam String userName) {
        UserDto ret = userService.getByUserName(userName);
        return ResponseEntity.ok(ret);
    }
}
