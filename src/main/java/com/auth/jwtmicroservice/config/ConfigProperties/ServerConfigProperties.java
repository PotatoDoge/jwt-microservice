package com.auth.jwtmicroservice.config.ConfigProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "server")
@Data
public class ServerConfigProperties {

    private String address;
}
