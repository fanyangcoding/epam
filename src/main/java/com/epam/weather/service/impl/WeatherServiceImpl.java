package com.epam.weather.service.impl;

import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.exception.BusinessException;
import com.epam.weather.response.ResultStatus;
import com.epam.weather.service.WeatherService;
import com.epam.weather.utils.Constants;
import com.google.common.collect.Lists;
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

    public final String INITIAL_CODE = "00000000";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Retryable(value = {RestClientException.class, BusinessException.class}, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public Optional<Integer> getTemperature(LocationDTO locationDTO) throws RestClientException, BusinessException {
        // 查省级代码
        String provinceCode = this.getCode(
                Constants.PROVINCE_CODE_HTML_PREFIX + "china" + Constants.HTML_SUFFIX, locationDTO.getProvince());
        if (INITIAL_CODE.equals(provinceCode)) {
            throw new BusinessException(ResultStatus.FAIL.getCode(), "国内没有该省、直辖市或自治区");
        }
        // 查市级代码
        String cityCode = this.getCode(
                Constants.CITY_CODE_HTML_PREFIX + provinceCode + Constants.HTML_SUFFIX, locationDTO.getCity());
        if (INITIAL_CODE.equals(cityCode)) {
            throw new BusinessException(ResultStatus.FAIL.getCode(), "省内没有该市");
        }
        // 查区县代码
        String countyCode = this.getCode(
                Constants.COUNTY_CODE_HTML_PREFIX + provinceCode + cityCode + Constants.HTML_SUFFIX, locationDTO.getCounty());
        if (INITIAL_CODE.equals(countyCode)) {
            throw new BusinessException(ResultStatus.FAIL.getCode(), "市内没有该区、县");
        }
        // 获取温度
        String weatherUrl = Constants.WEATHER_COUNTY_HTML_PREFIX + provinceCode + cityCode + countyCode + Constants.HTML_SUFFIX;
        if (isMunicipality(locationDTO.getCity())) {
            // 直辖市的citycode和countycode位置反了！！！
            weatherUrl = Constants.WEATHER_COUNTY_HTML_PREFIX + provinceCode + countyCode + cityCode + Constants.HTML_SUFFIX;
        }
        Integer temp = this.getWeather(weatherUrl);
        Optional.ofNullable(temp).orElseThrow(() ->
                new BusinessException(ResultStatus.FAIL.getCode(), "没有该地区的气温"));
        return Optional.of(temp);
    }

    public Integer getWeather(String url) {
        Gson gson = new Gson();
        String weatherCodeStr = restTemplate.getForObject(url, String.class);
        JsonObject weatherObject = gson.fromJson(weatherCodeStr, JsonObject.class);
        JsonObject weatherInfoObject = weatherObject.getAsJsonObject("weatherinfo");
        String tempStr = weatherInfoObject.get("temp").getAsString();
        double tempDouble = Double.parseDouble(tempStr);
        return new Long(Math.round(tempDouble)).intValue();
    }


    public String getCode(String url, String location) {
        String code = INITIAL_CODE;
        String codeStr = restTemplate.getForObject(url, String.class);
        Gson gson = new Gson();
        JsonObject locationObject = gson.fromJson(codeStr, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : locationObject.entrySet()) {
            if (location.equals(entry.getValue().getAsString())) {
                code = entry.getKey();
                break;
            }
        }
        return code;
    }

    // 是否是直辖市
    public boolean isMunicipality(String city) {
        return Lists.newArrayList(Constants.MUNICIPALITY).contains(city);
    }

    @Recover
    public Optional<Integer> recoverRestClientException(RestClientException e, LocationDTO locationDTO) {
        log.error("网络原因，重连失败");
        return Optional.empty();
    }

    @Recover
    public Optional<Integer> recoverBusinessException(BusinessException e, LocationDTO locationDTO) {
        log.error("业务异常, 重连失败");
        throw new BusinessException(ResultStatus.FAIL.getCode(), e.getMsg());
    }
}
