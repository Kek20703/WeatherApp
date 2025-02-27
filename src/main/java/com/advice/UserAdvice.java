package com.advice;

import com.dto.request.SignUpUserDto;
import com.exception.UnauthorizedException;
import com.exception.UserAlreadyExistsException;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Order(1)
@ControllerAdvice
public class UserAdvice {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ModelAndView handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return createModelAndView("sign-up", "Account with this login already exists.");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(UnauthorizedException e) {
        return createModelAndView("sign-up", "If you don't have an account, please sign up.");
    }

    private ModelAndView createModelAndView(String viewName, String errorMessage) {
        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("errorMessage", errorMessage);
        mav.addObject("signUpUserDto", SignUpUserDto.builder().build());
        return mav;
    }
}
