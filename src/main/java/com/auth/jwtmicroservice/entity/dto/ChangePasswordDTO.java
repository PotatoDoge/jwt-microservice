package com.auth.jwtmicroservice.entity.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {

    private String token;
    private String newPassword;

}
