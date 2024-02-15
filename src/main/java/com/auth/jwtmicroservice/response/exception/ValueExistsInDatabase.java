package com.auth.jwtmicroservice.response.exception;

public class ValueExistsInDatabase extends RuntimeException{

    public ValueExistsInDatabase(String message){
        super(message);
    }
}
