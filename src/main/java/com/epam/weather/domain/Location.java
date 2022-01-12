package com.epam.weather.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @ApiModelProperty(required = true, notes = "省")
    private String province;
    @ApiModelProperty(required = true, notes = "市")
    private String city;
    @ApiModelProperty(required = true, notes = "县")
    private String county;
}
