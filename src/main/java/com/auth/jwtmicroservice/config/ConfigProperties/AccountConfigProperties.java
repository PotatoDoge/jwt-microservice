package com.auth.jwtmicroservice.config.ConfigProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "account")
@Data
public class AccountConfigProperties {
    private boolean emailValidation;
    private int validationTokenDurationInMinutes;
}
