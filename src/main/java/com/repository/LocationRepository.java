package com.repository;

import com.entity.Location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByUserIdAndLatitudeAndLongitude(Integer user, BigDecimal longitude, BigDecimal latitude);
    @Modifying
    @Transactional
    void deleteByUserIdAndLatitudeAndLongitude(Integer user, BigDecimal latitude, BigDecimal longitude);
}
