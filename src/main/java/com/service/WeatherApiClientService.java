package com.service;

import com.dto.response.LocationResponseDto;
import com.dto.response.WeatherResponseDto;
import com.entity.Location;

import java.util.List;

public interface WeatherApiClientService {
    List<LocationResponseDto> getLocationsByName(String location);

    WeatherResponseDto getWeatherForLocation(Location location);
}
