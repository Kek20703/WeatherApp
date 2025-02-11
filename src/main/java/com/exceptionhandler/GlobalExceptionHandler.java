package com.exceptionhandler;

import com.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleNonAuthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", request.getContextPath()+"/signIn" );  // указываем URL для редиректа
        return ResponseEntity.status(HttpStatus.FOUND)  // HTTP 302
                .headers(headers)
                .body("Redirecting to /signIn");
    }
}
