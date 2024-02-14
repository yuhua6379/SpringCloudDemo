package com.springcloud.demo.auth.dto;

import lombok.Data;

@Data
public class VerifyDto extends ReturnBase {
    String userName;

    public static VerifyDto fail() {
        VerifyDto ret = new VerifyDto();

        ret.setErrorCode(ErrorCode.TOKEN_INVALID);
        ret.setErrorMessage("invalid token");
        ret.setStatus(Status.FAIL);

        return ret;
    }

    public static VerifyDto success(String userName) {
        VerifyDto ret = new VerifyDto();

        ret.setErrorCode(ErrorCode.SUCCESS);
        ret.setUserName(userName);
        ret.setStatus(Status.SUCCESS);

        return ret;
    }
}
