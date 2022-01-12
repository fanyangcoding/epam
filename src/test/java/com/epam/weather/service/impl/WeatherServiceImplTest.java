package com.epam.weather.service.impl;

import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.SocketException;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void getTemperature() throws SocketException {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince("江苏");
        locationDTO.setCounty("苏州");
        locationDTO.setCity("吴江");
        weatherService.getTemperature(locationDTO);
    }

}