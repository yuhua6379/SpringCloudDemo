package com.springcloud.demo.auth.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UserTokenApplicationDto {
    @Size(min = 3, max = 64)
    String userName;

    @Size(min = 6, max = 20)
    String passWord;
}
