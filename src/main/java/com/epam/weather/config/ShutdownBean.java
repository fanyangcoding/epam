package com.epam.weather.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

@Slf4j
public class ShutdownBean {
    @PreDestroy
    public void preDestroy() {
        log.info("ShutdownBean is destroyed");
    }
}