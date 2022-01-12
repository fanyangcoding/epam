package com.epam.weather.config;

import com.epam.weather.exception.BusinessException;
import com.epam.weather.response.ResultStatus;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class LimitInterceptor extends HandlerInterceptorAdapter {
    public enum LimitType {
        DROP, // 丢弃
        WAIT  // 等待
    }

    private RateLimiter limiter;

    private LimitType limitType = LimitType.DROP;

    public LimitInterceptor() {
        this.limiter = RateLimiter.create(1);
    }

    public LimitInterceptor(int tps, LimitInterceptor.LimitType limitType) {
        this.limiter = RateLimiter.create(tps);
        this.limitType = limitType;
    }

    public LimitInterceptor(double permitPerSecond, LimitInterceptor.LimitType limitType) {
        this.limiter = RateLimiter.create(permitPerSecond, 1000, TimeUnit.MILLISECONDS);
        this.limitType = limitType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (limitType.equals(LimitType.DROP)) {
            if (limiter.tryAcquire()) {
                return super.preHandle(request, response, handler);
            }
        }
//        else {
//            double count = limiter.acquire();
//            return super.preHandle(request, response, handler);
//        }
        throw new BusinessException(ResultStatus.FAIL.getCode(), "达到请求上限，开始限流");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    public RateLimiter getLimiter() {
        return limiter;
    }

    public void setLimiter(RateLimiter limiter) {
        this.limiter = limiter;
    }
}
