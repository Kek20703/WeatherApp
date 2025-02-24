package com.service;

import com.dto.response.LocationResponseDto;
import com.dto.response.WeatherResponseDto;
import com.entity.Location;
import com.exception.InvalidApiRequestException;
import com.exception.JsonDeserializationException;
import com.exception.WeatherApiException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateWeatherApiClientService implements WeatherApiClientService {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("API_KEY");
    private final RestTemplate restTemplate;

    public WeatherResponseDto getWeatherForLocation(Location location) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/data/2.5/weather").queryParam("lon", location.getLongitude()).queryParam("lat", location.getLatitude()).queryParam("appid", API_KEY).queryParam("units", "metric").toUriString();
        try {
            ResponseEntity<WeatherResponseDto> response = restTemplate.getForEntity(url, WeatherResponseDto.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new InvalidApiRequestException("Client error: ");
        } catch (HttpServerErrorException e) {
            throw new WeatherApiException("Server error: ");
        } catch (Exception e) {
            throw new JsonDeserializationException("Error deserializing response");
        }
    }

    public List<LocationResponseDto> getLocationsByName(String location) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.openweathermap.org/geo/1.0/direct").queryParam("q", location).queryParam("limit", 5).queryParam("appid", API_KEY).toUriString();
        try {
            ResponseEntity<List<LocationResponseDto>> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<LocationResponseDto>>() {
            });

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new InvalidApiRequestException("Client error: ");
        } catch (HttpServerErrorException e) {
            throw new WeatherApiException("Server error: ");
        } catch (Exception e) {
            throw new JsonDeserializationException("Error deserializing response");
        }
    }
}
