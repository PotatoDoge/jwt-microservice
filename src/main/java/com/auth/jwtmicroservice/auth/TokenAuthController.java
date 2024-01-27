package com.auth.jwtmicroservice.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("tokenAuth")
public class TokenAuthController {

    @GetMapping("/authenticate")
    public ResponseEntity<String> validateToken(){
        return ResponseEntity.ok().body("Token authenticated correctly");
    }

}
