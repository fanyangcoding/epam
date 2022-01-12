package com.epam.weather.service.impl;

import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.exception.BusinessException;
import com.epam.weather.service.WeatherService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void getTemperature() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince("江苏");
        locationDTO.setCounty("苏州");
        locationDTO.setCity("苏州");
        Assertions.assertNotNull(weatherService.getTemperature(locationDTO));
    }

    @Test(expected = BusinessException.class)
    public void getTemperature_exception() throws BusinessException {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince("江苏");
        locationDTO.setCounty("苏州");
        locationDTO.setCity("suzhou");
        weatherService.getTemperature(locationDTO);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getTemperature_exception1() throws BusinessException {
        expectedEx.expect(BusinessException.class);
        expectedEx.expectMessage("省内没有该市");
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince("浙江");
        locationDTO.setCounty("苏州");
        locationDTO.setCity("苏州");
        weatherService.getTemperature(locationDTO);
    }


}