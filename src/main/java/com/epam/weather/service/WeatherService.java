package com.epam.weather.service;

import com.epam.weather.domain.dto.LocationDTO;

import java.util.Optional;

public interface WeatherService {
    Optional<Integer> getTemperature(LocationDTO locationDTO);
}
