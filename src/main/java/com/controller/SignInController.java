package com.controller;

import com.dto.request.UserLoginDto;
import com.entity.UserSession;
import com.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private final UserService userAuthService;

    private HttpServletRequest request;


    @GetMapping("/signIn")
    public String getSignInPage(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        if(sessionId != null) {
            return "redirect:/home";
        }
        return "sign-in";
    }
    @PostMapping("/signIn")
    public String signIn(@ModelAttribute UserLoginDto credentialsDto, Model model, HttpServletResponse response) {
        Optional<UserSession> session = userAuthService.login(credentialsDto);
        if (session.isPresent()) {
            Cookie sessionCookie = new Cookie("SESSIONID", session.get().getId().toString());
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(86400);
            response.addCookie(sessionCookie);
            model.addAttribute("sessionId", session.get().getId());
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "sign-in";
        }
    }
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "SESSIONID") String sessionId , HttpServletResponse response) {
        userAuthService.logout(UUID.fromString(sessionId));
        Cookie cookie = new Cookie("SESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:signIn";
    }
}
