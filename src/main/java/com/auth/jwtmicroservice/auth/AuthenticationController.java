package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.config.FrontendConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final FrontendConfigProperties frontendConfigProperties;

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
    public ResponseEntity<Void> activateAccount(@PathVariable String token){
        String confirmationTokenResponse = service.activateAccount(token);
        String redirectTo = frontendConfigProperties.getTokenValidationScreen() + "?confirmation-message="+ confirmationTokenResponse;
        redirectTo = service.encodeUri(redirectTo);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectTo).build();
    }

}
