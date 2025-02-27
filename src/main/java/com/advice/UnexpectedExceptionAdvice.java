package com.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(2)
@ControllerAdvice
public class UnexpectedExceptionAdvice {
    @ExceptionHandler
    public String handleException(Exception ex, HttpServletRequest request) {
        return "error";
    }

}
