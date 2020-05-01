package com.santander.meetup.controller;

import com.santander.meetup.dto.response.DayForecastDto;
import com.santander.meetup.endpoint.WeatherEndpoint;
import com.santander.meetup.service.WeatherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Weather")
@RestController
@RequestMapping(value = WeatherEndpoint.BASE)
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @ApiOperation(value = "Retrieves a 16-day forecast in 1-day intervals")
    @GetMapping(WeatherEndpoint.DAILY_FORECAST)
    public ResponseEntity<List<DayForecastDto>> get16DaysDailyForecast() {
        return new ResponseEntity<>(weatherService.getDailyForecast(), HttpStatus.CREATED);
    }
}
