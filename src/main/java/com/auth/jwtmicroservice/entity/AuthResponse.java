package com.auth.jwtmicroservice.entity;

import lombok.Data;

@Data
public class AuthResponse {

    private String email;
    private String fullName;
    private Long id;

}
