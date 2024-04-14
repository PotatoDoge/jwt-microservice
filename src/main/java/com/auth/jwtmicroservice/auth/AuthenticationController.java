package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.entity.dto.ResetPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Insecure endpoint that registers new users
     * @param request {username, password , role}
     * @return authorized and token in header if correct request, error if not
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        service.register(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION).body(null);
    }

    /**
     * Insecure endpoint that validates users
     * @param request {username, password}
     * @return authorized and token in header if correct request, error if not
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("activateAccount/{token}")
    public ResponseEntity<Map<String,String>> activateAccount(@PathVariable String token){
        String confirmationTokenResponse = service.activateAccount(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", confirmationTokenResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("resetPassword")
    public ResponseEntity<Map<String,String>> sendResetPasswordEmail(@RequestBody ResetPasswordDTO resetPasswordDTO){
        Map<String, String> response = new HashMap<>();
        response.put("message", service.sendResetPasswordMail(resetPasswordDTO));
        return ResponseEntity.ok().body(response);
    }

}
