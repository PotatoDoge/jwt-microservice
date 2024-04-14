package com.auth.jwtmicroservice.service;

import com.auth.jwtmicroservice.entity.ResetPasswordToken;
import com.auth.jwtmicroservice.repository.ResetPasswordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChangePasswordService {

    private final ResetPasswordRepository resetPasswordRepository;

    public void save(ResetPasswordToken resetPasswordToken) {
        resetPasswordRepository.save(resetPasswordToken);
    }

    public Optional<ResetPasswordToken> getToken(String token) {
        return resetPasswordRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        resetPasswordRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
