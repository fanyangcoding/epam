package com.epam.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownConfig {
    @Bean
    public ShutdownBean getTerminateBean() {
        return new ShutdownBean();
    }
}
