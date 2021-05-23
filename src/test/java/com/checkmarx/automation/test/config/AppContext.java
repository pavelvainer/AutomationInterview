package com.checkmarx.automation.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.checkmarx"})
public class AppContext {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}