package com.epam.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class APIException extends RuntimeException{
    private String errorCode;
    private String msg;
}
