package com.controller;

import com.dto.request.SignUpUserDto;
import com.dto.request.UserLoginDto;
import com.exception.UserAlreadyExistsException;
import com.service.UserService;
import com.util.validator.SignUpUserDTOValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    private static final String SIGN_UP_VIEW = "sign-up";
    private final UserService userAuthService;
    private final SignUpUserDTOValidator userCredentialsValidator;


    @GetMapping("/signUp")
    public String getSignUpPage(@ModelAttribute("userRegistrationDto") SignUpUserDto userRegistrationDto, Model model) {
        model.addAttribute("signUpUserDto", SignUpUserDto.builder().build());
        return SIGN_UP_VIEW;
    }

    @PostMapping("/signUp")
    public String singUp(@ModelAttribute("signUpUserDto") @Validated SignUpUserDto credentials, BindingResult bindingResult) {
        userCredentialsValidator.validate(credentials, bindingResult);
        if (bindingResult.hasErrors()) {
            return SIGN_UP_VIEW;
        }
        try {
            userAuthService.createUser(new UserLoginDto(credentials.getUsername(), credentials.getPassword()));
        } catch (UserAlreadyExistsException exception) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
            return SIGN_UP_VIEW;
        }
        return SIGN_UP_VIEW;
    }
}
