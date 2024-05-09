package com.leeforgiveness.memberservice.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private final BaseResponseStatus errorCode;

    public BaseException(BaseResponseStatus errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
