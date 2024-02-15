package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.config.JwtService;
import com.auth.jwtmicroservice.entity.User;
import com.auth.jwtmicroservice.repository.UserRepository;
import com.auth.jwtmicroservice.response.exception.ValueExistsInDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers new user
     * @param request {username, password, role}
     * @return jwt based on new user
     */
    public AuthenticationResponse register(RegisterRequest request){
        User user = new User(request.getFullName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getRole());
        boolean emailAlreadyRegister = repository.existsByEmail(request.getEmail());
        if(emailAlreadyRegister){
            throw new ValueExistsInDatabase("Email already registered!");
        }
        repository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    /**
     * Authenticates that user exists in database (log in)
     * @param request {username, password}
     * @return jwt based on existing user
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = repository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

}
