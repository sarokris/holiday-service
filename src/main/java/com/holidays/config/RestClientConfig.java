package com.holidays.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${api.base-url}")
    private String baseUrl;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }


}
