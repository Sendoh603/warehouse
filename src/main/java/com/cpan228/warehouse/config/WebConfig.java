package com.cpan228.warehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final DistributionCentreProperties distributionCentreProperties;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .basicAuthentication(
                distributionCentreProperties.getUsername(),
                distributionCentreProperties.getPassword()
            )
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}