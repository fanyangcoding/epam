package com.epam.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class APIException extends RuntimeException {
    private String errorCode;
    private String msg;
}
