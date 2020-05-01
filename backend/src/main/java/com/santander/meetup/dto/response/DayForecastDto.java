package com.santander.meetup.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class DayForecastDto implements Serializable {

    @JsonAlias("datetime")
    private LocalDate day;

    @JsonAlias("temp")
    @ApiParam("Average Temperature")
    private double temperature;
}
