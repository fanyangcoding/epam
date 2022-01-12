package com.epam.weather.controller;

import com.epam.weather.domain.Location;
import com.epam.weather.domain.dto.LocationDTO;
import com.epam.weather.response.ResultModel;
import com.epam.weather.response.ResultStatus;
import com.epam.weather.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/weather")
@Api(value = "查询天气")
@Validated
@Slf4j
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/temperature", produces = "application/json")
    @ResponseBody
    @ApiOperation(value = "get temperature")
    public ResultModel<Integer> getTemperature(@NotBlank(message = "不能为空") @RequestParam(value = "province", defaultValue = "江苏") String province,
                                               @NotBlank(message = "不能为空") @RequestParam(value = "city", defaultValue = "苏州") String city,
                                               @NotBlank(message = "不能为空") @RequestParam(value = "county", defaultValue = "苏州") String county
    ) {
        Location location = new Location(province, city, county);
        Optional<Integer> tempOptional = weatherService.getTemperature(modelMapper.map(location, LocationDTO.class));
        return tempOptional.map(integer -> new ResultModel<>(ResultStatus.SUCCESS, integer)).orElseGet(() -> new ResultModel<>(ResultStatus.FAIL.getCode(), "重连失败，可能网络异常"));
    }
}
