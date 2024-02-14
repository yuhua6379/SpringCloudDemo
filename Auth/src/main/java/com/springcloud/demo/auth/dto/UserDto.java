package com.springcloud.demo.auth.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends ReturnBase {
    Long id;
    String userName;
    Date createTime;
}
