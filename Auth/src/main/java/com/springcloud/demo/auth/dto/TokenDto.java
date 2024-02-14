package com.springcloud.demo.auth.dto;

import lombok.Data;

@Data
public class TokenDto extends ReturnBase {
    String token;

    public static TokenDto fail() {
        TokenDto ret = new TokenDto();

        ret.setErrorCode(ErrorCode.USER_PASS_INCORRECT);
        ret.setErrorMessage("Incorrect username or password");
        ret.setStatus(Status.FAIL);

        return ret;
    }

    public static TokenDto success(String token) {
        TokenDto ret = new TokenDto();

        ret.setErrorCode(ErrorCode.SUCCESS);
        ret.setToken(token);
        ret.setStatus(Status.SUCCESS);

        return ret;
    }
}
