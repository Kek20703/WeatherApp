package com.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UnauthorizedException extends RuntimeException{
    private final String message;
}
