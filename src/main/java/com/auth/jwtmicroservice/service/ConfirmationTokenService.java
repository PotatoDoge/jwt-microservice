package com.auth.jwtmicroservice.service;

import com.auth.jwtmicroservice.entity.ConfirmationToken;
import com.auth.jwtmicroservice.entity.User;
import com.auth.jwtmicroservice.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public boolean validateIfLastTokenIsValidByUserId(User user){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findLatestTokenByUser(user).orElse(null);
        if(confirmationToken == null){
            return false;
        }
        return LocalDateTime.now().isBefore(confirmationToken.getExpiresAt());
    }

}
