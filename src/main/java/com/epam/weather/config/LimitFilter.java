package com.epam.weather.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class LimitFilter implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LimitInterceptor(100, LimitInterceptor.LimitType.DROP))
                .addPathPatterns("/api/v1/weather/temperature");
    }
}
