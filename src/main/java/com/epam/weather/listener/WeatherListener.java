package com.epam.weather.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Slf4j
public class WeatherListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Servlet Context 初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet Context 销毁");
    }
}
