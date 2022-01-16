package com.epam.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/v1/weather/")
public class AopLogController {
    @GetMapping("/aoptest")
    public String aVoid() {
        return "hello aop test";
    }
}
