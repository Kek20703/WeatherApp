package com.controller;

import com.dto.request.UserLoginDto;
import com.entity.UserSession;
import com.service.UserService;
import com.util.SessionCookieUtil;
import jakarta.servlet.http.Cookie;
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
    private static final String HOME_REDIRECT = "redirect:/home";
    private static final String SIGN_IN_REDIRECT = "redirect:signIn";
    private static final String SIGN_IN_VIEW = "sign-in";
    private final UserService userAuthService;

    @GetMapping("/signIn")
    public String getSignInPage(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        if (sessionId != null) {
            return HOME_REDIRECT;
        }
        return SIGN_IN_VIEW;
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute UserLoginDto credentialsDto, Model model, HttpServletResponse response) {
        Optional<UserSession> session = userAuthService.login(credentialsDto);
        if (session.isPresent()) {
            Cookie sessionCookie = SessionCookieUtil.getNewSessionCookie(session.get().getId().toString());
            response.addCookie(sessionCookie);
            model.addAttribute("sessionId", session.get().getId());
            return HOME_REDIRECT;
        } else {
            // TODO: пол логике вроде все ок. Но возникают проблемы с пониманием такой ошибки. Если нет сессии = неверные креды
            //   возможно стоит немного поменять либо определение неверности (выбросив исключение например)
            model.addAttribute("error", "Invalid credentials");
            return SIGN_IN_VIEW;
        }
    }

    @PostMapping("/logout")
    public String logout(@CookieValue(value = "SESSIONID") String sessionId, HttpServletResponse response) {
        // TODO: userAuthService плохое название для сервиса, так как сам класс имеет название просто UserService.
        //  старайся использовать создающие непоняток нейминги класса/интерфейса - объекта
        userAuthService.logout(UUID.fromString(sessionId));
        Cookie cookie = SessionCookieUtil.getEmptySessionCookie();
        response.addCookie(cookie);
        return SIGN_IN_REDIRECT;
    }
}
