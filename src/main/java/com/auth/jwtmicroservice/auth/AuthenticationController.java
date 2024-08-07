package com.auth.jwtmicroservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Insecure endpoint that registers new users
     * @param request {username, password , role}
     * @return authorized and token in header if correct request, error if not
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, response.getToken()).body(null);
    }

    /**
     * Insecure endpoint that validates users
     * @param request {username, password}
     * @return authorized and token in header if correct request, error if not
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, response.getToken()).body(null);
    }

}
