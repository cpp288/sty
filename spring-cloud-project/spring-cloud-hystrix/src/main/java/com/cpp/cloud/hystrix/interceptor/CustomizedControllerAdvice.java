package com.cpp.cloud.hystrix.interceptor;

import com.cpp.cloud.hystrix.controller.CustomizedController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeoutException;

/**
 * CustomizedController Advice
 *
 * @author chenjian
 * @date 2018-12-02 21:49
 */
@RestControllerAdvice(assignableTypes = CustomizedController.class)
public class CustomizedControllerAdvice {

    @ExceptionHandler(TimeoutException.class)
    public String onTimeoutException(TimeoutException ex) {
        return "Error";
    }
}
