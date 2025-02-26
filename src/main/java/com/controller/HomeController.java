package com.controller;

import com.dto.request.LocationDeleteRequestDto;
import com.dto.response.WeatherResponseDto;
import com.entity.User;
import com.service.LocationService;
import com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private static final String HOME_VIEW = "index";
    private static final String HOME_REDIRECT = "redirect:/home";
    private final UserService userService;
    private final LocationService locationService;

    @GetMapping("/home")
    public String home(@CookieValue("SESSIONID") String sessionId, Model model) {
        User user = userService.getUser(UUID.fromString(sessionId));
        List<WeatherResponseDto> weatherList = locationService.getWeatherList(user);
        model.addAttribute("username", user.getLogin());
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("locationDeleteRequest", new LocationDeleteRequestDto());
        return HOME_VIEW;
    }

    @PostMapping("/deleteLocation")
    public String deleteLocation(@CookieValue("SESSIONID") String sessionId,
                                 @ModelAttribute("locationDeleteRequest") LocationDeleteRequestDto locationDeleteRequest) {
        User user = userService.getUser(UUID.fromString(sessionId));
        locationService.removeLocation(user, locationDeleteRequest);
        return HOME_REDIRECT;

    }
}
