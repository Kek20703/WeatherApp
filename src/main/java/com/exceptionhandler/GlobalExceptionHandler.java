package com.exceptionhandler;

import com.exception.UnauthorizedException;
import com.exception.WeatherApiException;
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
        headers.add("Location", request.getContextPath()+"/signIn" );
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .body("Redirecting to /signIn");
    }
    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<String> handleWeatherApiException(WeatherApiException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ошибка при запросе погоды: " + ex.getResponseBody());
    }
    @ExceptionHandler
    public String handleException(Exception ex, HttpServletRequest request) {
        return "error";
    }
}
