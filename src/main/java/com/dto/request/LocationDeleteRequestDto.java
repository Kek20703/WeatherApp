package com.dto.request;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDeleteRequestDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;


}