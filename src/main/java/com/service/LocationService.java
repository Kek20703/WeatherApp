package com.service;

import com.dto.request.LocationDeleteRequestDto;
import com.dto.response.LocationResponseDto;
import com.dto.response.WeatherResponseDto;
import com.entity.Location;
import com.entity.User;

import java.util.List;

public interface LocationService {
    WeatherResponseDto getWeatherForLocation(Location location);

    void saveLocation(User user, LocationResponseDto location);

    void removeLocation(User user, LocationDeleteRequestDto locationDeleteRequest);

    List<WeatherResponseDto> getWeatherList(User user);

    List<LocationResponseDto> getAvailableLocations(String locationName, User user);
}
