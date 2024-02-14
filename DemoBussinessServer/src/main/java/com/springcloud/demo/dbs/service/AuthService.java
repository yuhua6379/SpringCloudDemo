package com.springcloud.demo.dbs.service;

import com.springcloud.demo.dbs.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
// name等效于host+port，是注册在Eureka的标识
// 这个标识被application.yml的spring.application.name定义
// path是endpoint，就是路径的第一项
@FeignClient(name = "auth", path = "/auth")
public interface AuthService {

    @GetMapping("/user/info")
    UserDto info(@RequestParam String userName);
}
