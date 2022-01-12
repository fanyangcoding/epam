package com.epam.weather.exception;

import com.epam.weather.response.ResultModel;
import com.epam.weather.response.ResultStatus;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author z003x4mx
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultModel<String> exceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        logger.error("service failed", e);
        return new ResultModel<>(ResultStatus.FAIL, "service failed, because: " + e.getMessage());
    }

    /**
     * Business Exception Handler
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ResultModel<?> businessExceptionHandler(HttpServletRequest httpServletRequest, BusinessException e) {
        logger.error("business exception", e);
        return new ResultModel<>(ResultStatus.FAIL, "business exception, because: " + e.getMsg());
    }

    @ResponseBody
    @ExceptionHandler(value = JsonSyntaxException.class)
    public ResultModel<?> jsonSyntaxException(HttpServletRequest httpServletRequest, JsonSyntaxException e) {
        logger.error("json syntax exception", e);
        return new ResultModel<>(ResultStatus.FAIL, "json syntax: " + e.getMessage());
    }
}
