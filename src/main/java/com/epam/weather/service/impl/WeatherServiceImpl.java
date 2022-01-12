package com.epam.weather.service.impl;

import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.exception.BusinessException;
import com.epam.weather.response.ResultStatus;
import com.epam.weather.service.WeatherService;
import com.epam.weather.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * 获取天气
 */

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Retryable(value = RestClientException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public Optional<Integer> getTemperature(LocationDTO locationDTO) throws RestClientException { // 查询温度
//        String responseStr = restTemplate.getForObject("http://jsonplaceholder.typicode.com/postss/1", String.class);
        // 查省级代码
        String provinceCode = null;
        String provCodeStr = restTemplate.getForEntity(Constants.PROVINCE_CODE_HTML_PREFIX + "china" + Constants.HTML_SUFFIX, String.class).getBody();
        Gson gson = new Gson();
        JsonObject provinceObject = gson.fromJson(provCodeStr, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : provinceObject.entrySet()) {
            if (locationDTO.getProvince().equals(entry.getValue().getAsString())) {
                provinceCode = entry.getKey();
                break;
            }
        }
        Optional.ofNullable(provinceCode).orElseThrow(() -> new BusinessException(ResultStatus.FAIL.getCode(), "没有该省"));
        // 查市级代码
        String cityCode = null;
        String cityCodeStr = restTemplate.getForObject(Constants.CITY_CODE_HTML_PREFIX + provinceCode + Constants.HTML_SUFFIX, String.class);
        JsonObject cityObject = gson.fromJson(cityCodeStr, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : cityObject.entrySet()) {
            if (locationDTO.getCity().equals(entry.getValue().getAsString())) {
                cityCode = entry.getKey();
                break;
            }
        }
        Optional.ofNullable(cityCode).orElseThrow(() -> new BusinessException(ResultStatus.FAIL.getCode(), "省内没有该市"));
        // 查县区代码
        String countyCode = null;
        String countyCodeStr = restTemplate.getForObject(Constants.COUNTY_CODE_HTML_PREFIX + provinceCode + cityCode + Constants.HTML_SUFFIX, String.class);
        JsonObject countyObject = gson.fromJson(countyCodeStr, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : countyObject.entrySet()) {
            if (locationDTO.getCounty().equals(entry.getValue().getAsString())) {
                countyCode = entry.getKey();
                break;
            }
        }
        Optional.ofNullable(countyCode).orElseThrow(() -> new BusinessException(ResultStatus.FAIL.getCode(), "市内没有该县/区"));
        // 获取温度
        String tempStr;
        String weatherCodeStr = restTemplate.getForObject(Constants.WEATHER_COUNTY_HTML_PREFIX + provinceCode + cityCode + countyCode + Constants.HTML_SUFFIX, String.class);
        JsonObject weatherObject = gson.fromJson(weatherCodeStr, JsonObject.class);
        JsonObject weatherInfoObject = weatherObject.getAsJsonObject("weatherinfo");
        tempStr = weatherInfoObject.get("temp").getAsString();
        Optional.ofNullable(tempStr).orElseThrow(() -> new BusinessException(ResultStatus.FAIL.getCode(), "没有该地区的温度"));
        double tempDouble = Double.parseDouble(tempStr);
        Integer temp = new Long(Math.round(tempDouble)).intValue();
        return Optional.of(temp);
    }

    @Recover
    public Optional<Integer> recover(RestClientException e, LocationDTO locationDTO) {
        log.error("重连失败");
        return Optional.empty();
    }
}
