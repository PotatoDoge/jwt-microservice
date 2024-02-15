package com.auth.jwtmicroservice.response.exception;

public class UnauthorizedUser extends RuntimeException{

    public UnauthorizedUser(String message){
        super(message);
    }
}