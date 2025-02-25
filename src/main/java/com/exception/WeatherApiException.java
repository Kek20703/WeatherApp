package com.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WeatherApiException extends RuntimeException {
    private final String responseBody;
}