package com.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponseDto {
    private Main main;
    private List<Weather> weather;
    private String name;
    private Sys sys;
    private Coord coord;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Main {
        private int temp;
        @JsonProperty("feels_like")
        private int feelsLike;
        @JsonProperty("temp_min")
        private int tempMin;
        @JsonProperty("temp_max")
        private int tempMax;
        private int humidity;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sys {
        private String country;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coord {
        private BigDecimal lat;
        private BigDecimal lon;
    }
}
