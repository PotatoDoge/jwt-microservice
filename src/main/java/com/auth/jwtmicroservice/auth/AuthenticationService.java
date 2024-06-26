package com.auth.jwtmicroservice.auth;

import com.auth.jwtmicroservice.config.ConfigProperties.AccountConfigProperties;
import com.auth.jwtmicroservice.config.JwtService;
import com.auth.jwtmicroservice.entity.ConfirmationToken;
import com.auth.jwtmicroservice.entity.dto.ChangePasswordDTO;
import com.auth.jwtmicroservice.entity.dto.ResetPasswordDTO;
import com.auth.jwtmicroservice.entity.ResetPasswordToken;
import com.auth.jwtmicroservice.entity.User;
import com.auth.jwtmicroservice.repository.UserRepository;
import com.auth.jwtmicroservice.response.exception.NotFoundInDatabase;
import com.auth.jwtmicroservice.response.exception.UnauthorizedUser;
import com.auth.jwtmicroservice.response.exception.ValueExistsInDatabase;
import com.auth.jwtmicroservice.service.ChangePasswordService;
import com.auth.jwtmicroservice.service.ConfirmationTokenService;
import com.auth.jwtmicroservice.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountConfigProperties accountConfigProperties;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailSenderService mailSenderService;
    private final ChangePasswordService changePasswordService;
    private final UserRepository userRepository;

    /**
     * Registers new user
     *
     * @param request {username, password, role}
     * @return jwt based on new user
     */
    public void register(RegisterRequest request) {
        User user = new User(request.getFullName(), request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getRole(), false, false);
        boolean emailAlreadyRegister = repository.existsByEmail(request.getEmail());
        if (emailAlreadyRegister) {
            throw new ValueExistsInDatabase("Email already registered!");
        }

        //Confirmation token disabled
        if (!accountConfigProperties.isEmailValidation()) {
            user.setEnabled(true);
            repository.save(user);
            return;
        }

        repository.save(user);
        sendToken(user);
    }

    /**
     * Authenticates that user exists in database (log in)
     *
     * @param request {username, password}
     * @return jwt based on existing user
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = repository.findByEmail(request.getEmail()).orElse(null);

        if (Objects.isNull(user)) {
            throw new UnauthorizedUser("Not a valid email or password");
        }

        if (!user.isEnabled()) {
            boolean validToken = confirmationTokenService.validateIfLastTokenIsValidByUserId(user);
            if (!validToken) {
                sendToken(user);
            }
            throw new UnauthorizedUser("Email registered but account is not enabled!");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new UnauthorizedUser("Not a valid email or password");
        }

        String token = jwtService.generateToken(user, user.getId());
        return new AuthenticationResponse(token);
    }

    public String getFullName(String email) {
        User user = repository.findByEmail(email).orElse(null);
        if (user == null) {
            return "";
        }
        return user.getFullName();
    }

    /**
     * Activates account based on confirmation token
     *
     * @param token confirmation token
     * @return confirmation response
     */
    @Transactional
    public String activateAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElse(null);

        if (confirmationToken == null) {
            return "Token not found";
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return "Email already confirmed";
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        confirmationTokenService.setConfirmedAt(token);
        repository.enableUser(confirmationToken.getUser().getEmail());
        return "Account activated!";
    }

    private void sendToken(User user) {
        String confToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(confToken, LocalDateTime.now(), LocalDateTime.now().plusMinutes(accountConfigProperties.getValidationTokenDurationInMinutes()), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // Send confirmation token via email
        mailSenderService.sendSimpleMessage(user, confToken);
    }

    public String sendResetPasswordMail(ResetPasswordDTO resetPasswordDTO) {
        User user = repository.findByEmail(resetPasswordDTO.getEmail()).orElse(null);
        if (user == null) {
            throw new UnauthorizedUser("Not a valid email");
        }
        String resetToken = UUID.randomUUID().toString();
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(resetToken, LocalDateTime.now(), LocalDateTime.now().plusMinutes(accountConfigProperties.getValidationTokenDurationInMinutes()), user);
        changePasswordService.save(resetPasswordToken);
        mailSenderService.sendResetPasswordMail(resetPasswordToken);
        return "Email sent!";
    }

    public String changePassword(ChangePasswordDTO changePasswordDTO) {
        ResetPasswordToken resetPasswordToken = changePasswordService.getToken(changePasswordDTO.getToken()).orElse(null);
        if(resetPasswordToken == null) {
            throw new NotFoundInDatabase("Reset password token not found");
        }

        if (resetPasswordToken.getConfirmedAt() != null) {
            throw new ValueExistsInDatabase("Token already used");
        }

        LocalDateTime expiredAt = resetPasswordToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ValueExistsInDatabase("Token expired");
        }

        changePasswordService.setConfirmedAt(changePasswordDTO.getToken());

        User user = resetPasswordToken.getUser();

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return "New password set";
    }
}
