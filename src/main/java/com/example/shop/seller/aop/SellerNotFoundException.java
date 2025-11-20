package com.example.shop.seller.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SellerNotFoundException extends IllegalArgumentException {
    public SellerNotFoundException(UUID id){
        super("Seller not found with id" + id);
    }
}
