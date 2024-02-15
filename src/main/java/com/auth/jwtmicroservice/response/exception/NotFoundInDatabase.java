package com.auth.jwtmicroservice.response.exception;

public class NotFoundInDatabase extends RuntimeException{

    public NotFoundInDatabase(String message){
        super(message);
    }
}