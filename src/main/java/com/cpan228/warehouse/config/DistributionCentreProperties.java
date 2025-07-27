package com.cpan228.warehouse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "warehouse.distribution-centre")
public class DistributionCentreProperties {
    private String url = "http://localhost:8081";
    private String username = "admin";
    private String password = "admin123";
    private double latitude = 43.7289; // Default latitude (example: Toronto)
    private double longitude = -79.6076; // Default longitude (example: Toronto)

}