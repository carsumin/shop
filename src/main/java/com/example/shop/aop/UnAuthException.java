package com.example.shop.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthException extends RuntimeException {
    public UnAuthException(String msg){
        super(msg);
    }
}
