package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private Role role;

}
