package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.config.JwtService;
import com.auth.jwtmicroservice.entity.AuthResponse;
import com.auth.jwtmicroservice.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("tokenAuth")
@AllArgsConstructor
public class TokenAuthController {

    private JwtService jwtService;

    @GetMapping("/authenticate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String token){
        String jwt = token.substring(7);
        AuthResponse user = new AuthResponse();
        user.setId(Long.valueOf(jwtService.extractUserId(jwt)));
        user.setEmail(jwtService.extractUsername(jwt));
        return ResponseEntity.ok().body(user);
    }
}
