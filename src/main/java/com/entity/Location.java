package com.entity;

import com.dto.response.LocationResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "locations")
@Getter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Setter
    // TODO: ...
    private int id;
    @Column(name = "Name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    private User user;
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @Column(name = "Longitude")
    private BigDecimal longitude;

    public void setName(String  name) {
        // validate
        // set
    }

    public boolean compare(LocationResponseDto locationResponseDto) {
        return this.name.equals(locationResponseDto.getName()) &&
                this.latitude.compareTo(locationResponseDto.getLat()) == 0 &&
                this.longitude.compareTo(locationResponseDto.getLon()) == 0;
    }

}
