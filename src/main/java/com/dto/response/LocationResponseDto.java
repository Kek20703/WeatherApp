package com.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponseDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}