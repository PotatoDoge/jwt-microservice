package com.auth.jwtmicroservice.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
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
        HttpHeaders headers = new HttpHeaders();
        //headers.add(HttpHeaders.AUTHORIZATION, response.getToken());
        //headers.add("Access-Control-Allow-Headers", "Authorization, Content-Type");
        //return ResponseEntity.ok().headers(headers).body(null);
        return ResponseEntity.ok(response);
    }

}
