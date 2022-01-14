package com.epam.weather.service.impl;

import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.exception.BusinessException;
import com.epam.weather.service.WeatherService;
import com.epam.weather.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceImplTest {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private RestTemplate restTemplate;

    public final String INITIAL_CODE = "00000000";

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
        locationDTO.setCounty("无锡");
        locationDTO.setCity("苏州");
        weatherService.getTemperature(locationDTO);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void getTemperature_exception_expected() {
        expectedEx.expect(BusinessException.class);
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setProvince("江苏");
        locationDTO.setCounty("苏州");
        locationDTO.setCity("suzhou");
        weatherService.getTemperature(locationDTO);
    }

    @Test
    public void isMunicipality() {
        WeatherServiceImpl weatherServiceImpl = new WeatherServiceImpl();
        boolean exist = weatherServiceImpl.isMunicipality("北京");
        Assertions.assertTrue(exist);

        boolean noExist = weatherServiceImpl.isMunicipality("南京");
        Assertions.assertFalse(noExist);
    }

    @Test
    public void getCode() {
        String code = INITIAL_CODE;
        String getProvCodeUrl = Constants.PROVINCE_CODE_HTML_PREFIX + "china" + Constants.HTML_SUFFIX;
        String getCityCodeUrl = Constants.CITY_CODE_HTML_PREFIX + "10119" + Constants.HTML_SUFFIX;
        String getCountyCodeUrl = Constants.COUNTY_CODE_HTML_PREFIX + "10119" + "04" + Constants.HTML_SUFFIX;
        String codeStr = restTemplate.getForObject(getProvCodeUrl, String.class);
        Gson gson = new Gson();
        JsonObject locationObject = gson.fromJson(codeStr, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : locationObject.entrySet()) {
            if ("江苏".equals(entry.getValue().getAsString())) {
                code = entry.getKey();
                break;
            }
        }
        Assertions.assertEquals("10119", code);
//        Assertions.assertEquals("04", code);
//        Assertions.assertEquals("01", code);
    }

    @Test
    public void getWeather() {
        String provinceCode = "10119";
        String cityCode = "04";
        String countyCode = "01";
        Gson gson = new Gson();
        String weatherCodeStr = restTemplate.getForObject(Constants.WEATHER_COUNTY_HTML_PREFIX
                + provinceCode
                + cityCode
                + countyCode
                + Constants.HTML_SUFFIX, String.class);
        JsonObject weatherObject = gson.fromJson(weatherCodeStr, JsonObject.class);
        JsonObject weatherInfoObject = weatherObject.getAsJsonObject("weatherinfo");
        String tempStr = weatherInfoObject.get("temp").getAsString();
        double tempDouble = Double.parseDouble(tempStr);
        Integer temp = new Long(Math.round(tempDouble)).intValue();

        Assertions.assertNotNull(temp);

    }


}