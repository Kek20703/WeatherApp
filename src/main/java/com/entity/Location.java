package com.entity;

import com.dto.response.LocationResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "locations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"UserId", "Latitude", "Longitude"})})
@Getter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "Name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private User user;
    @Column(name = "Latitude")
    private BigDecimal latitude;
    @Column(name = "Longitude")
    private BigDecimal longitude;

    public boolean compare(LocationResponseDto locationResponseDto) {
        return this.name.equals(locationResponseDto.getName()) &&
                this.latitude.compareTo(locationResponseDto.getLat()) == 0 &&
                this.longitude.compareTo(locationResponseDto.getLon()) == 0;
    }

    public void setLatitude(BigDecimal latitude) {
        if (latitude != null) {
            this.latitude = latitude;
        }

    }

    public void setLongitude(BigDecimal longitude) {
        if (longitude != null) {
            this.longitude = longitude;
        }
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }
}
