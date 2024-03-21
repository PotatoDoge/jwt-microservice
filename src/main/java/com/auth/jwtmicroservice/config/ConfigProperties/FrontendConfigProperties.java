package com.auth.jwtmicroservice.config.ConfigProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "frontend")
@Data
public class FrontendConfigProperties {

    private String tokenValidationScreen;

}
