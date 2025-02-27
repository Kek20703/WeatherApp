package com.service;

import com.dto.request.LocationDeleteRequestDto;
import com.dto.response.LocationResponseDto;
import com.dto.response.WeatherResponseDto;
import com.entity.Location;
import com.entity.User;
import com.exception.LocationAlreadyExistsException;
import com.exception.LocationDoesNotExistsException;
import com.mapper.LocationMapper;
import com.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final WeatherApiClientService weatherApiClient;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public WeatherResponseDto getWeatherForLocation(Location location) {
        WeatherResponseDto weatherDto = weatherApiClient.getWeatherForLocation(location);
        weatherDto.getCoord().setLat(location.getLatitude());
        weatherDto.getCoord().setLon(location.getLongitude());
        weatherDto.setName(location.getName());
        return weatherDto;
    }

    @Override
    public List<WeatherResponseDto> getWeatherList(User user) {
        List<WeatherResponseDto> weatherList = new ArrayList<>();
        user.getLocations().forEach(location -> weatherList.add(getWeatherForLocation(location)));
        return weatherList;
    }

    @Override
    @Transactional
    public void saveLocation(User user, LocationResponseDto locationDto) {
        Location location = locationMapper.LocationResponseDtoToLocation(locationDto);
        try {
            location.setUser(user);
            locationRepository.save(location);
        } catch (DataIntegrityViolationException e) {
            throw new LocationAlreadyExistsException("User already picked this location");
        }

    }

    @Override
    public void removeLocation(User user, LocationDeleteRequestDto locationDeleteRequest) {
        try {
            locationRepository.deleteByUserIdAndLatitudeAndLongitude(user.getId(), locationDeleteRequest.getLat(), locationDeleteRequest.getLon());
        } catch (DataIntegrityViolationException e) {
            throw new LocationDoesNotExistsException("Location doesn't exist");
        }

    }

    @Override
    public List<LocationResponseDto> getAvailableLocations(String locationName, User user) {
        List<LocationResponseDto> locations = getLocationsByName(locationName);
        List<Location> addedLocations = user.getLocations();
        locations.removeIf(locationResponseDto -> addedLocations.stream().anyMatch(location -> location.getName().equals(locationResponseDto.getName()) && location.getLatitude().compareTo(locationResponseDto.getLat()) == 0 && location.getLongitude().compareTo(locationResponseDto.getLon()) == 0));
        return locations;
    }

    private List<LocationResponseDto> getLocationsByName(String locationName) {
        return weatherApiClient.getLocationsByName(formatLocationName(locationName));
    }

    private String formatLocationName(String locationName) {
        return locationName.trim().replace(" ", "-");
    }


}
