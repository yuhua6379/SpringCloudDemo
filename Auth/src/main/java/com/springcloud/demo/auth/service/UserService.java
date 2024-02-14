package com.springcloud.demo.auth.service;

import com.springcloud.demo.auth.dto.ErrorCode;
import com.springcloud.demo.auth.dto.Status;
import com.springcloud.demo.auth.dto.UserDto;
import com.springcloud.demo.auth.entity.User;
import com.springcloud.demo.auth.repository.UserRepo;
import com.springcloud.demo.auth.security.JwtUtils;
import com.springcloud.demo.auth.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtils jwtUtils;

    public User verifyUserWithPassword(String userName, String passWord) {
        Optional<User> user = userRepo.findByUserNameAndPassWord(userName, MD5Util.getMD5Str(passWord + "_SALT"));
        return user.orElse(null);
    }

    public String parseUserNameFromToken(String token){
        return jwtUtils.getUserNameFromJwtToken(token);
    }

    public boolean verifyToken(String token) {
        return jwtUtils.validateJwtToken(token);
    }

    public String generateToken(User user) {
        return jwtUtils.generateToken(user);
    }

    public UserDto getByUserName(String userName) {
        User user = userRepo.findByUserName(userName).orElse(null);
        UserDto userDto = new UserDto();
        if (user == null) {
            userDto.setStatus(Status.FAIL);
            userDto.setErrorCode(ErrorCode.USER_NOT_FOUND);
            userDto.setErrorMessage("It was supposed to be found but not.");
            return userDto;
        } else {

            userDto.setCreateTime(user.getCreateTime());
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());

            return userDto;
        }
    }
}
