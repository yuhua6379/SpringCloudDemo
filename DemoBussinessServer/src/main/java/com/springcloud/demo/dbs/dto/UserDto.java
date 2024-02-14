package com.springcloud.demo.dbs.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    Long id;
    String userName;
    Date createTime;
}
