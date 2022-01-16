package com.epam.weather.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(urlPatterns = "/*")
@Slf4j
public class WeatherFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        WeatherRequestWrapper weatherRequestWrapper = new WeatherRequestWrapper((HttpServletRequest) servletRequest);
        Map<String, String[]> parameterMap = new HashMap<>(weatherRequestWrapper.getParameterMap());
        String[] county = parameterMap.get(("county"));
        for (int i = 0; i < county.length; i++) {
            if ("suzhou".equals(county[i])) {
                county[i] = "苏州";
                log.info("suzhou已经被替换成苏州");
            }
        }
        parameterMap.put("county", county);
        filterChain.doFilter(weatherRequestWrapper, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
