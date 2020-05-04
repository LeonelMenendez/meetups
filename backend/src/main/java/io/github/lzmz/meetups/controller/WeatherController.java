package io.github.lzmz.meetups.controller;

import io.github.lzmz.meetups.dto.response.DayForecastDto;
import io.github.lzmz.meetups.endpoint.WeatherEndpoint;
import io.github.lzmz.meetups.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Weather")
@RestController
@RequestMapping(value = WeatherEndpoint.BASE)
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(summary = "Retrieves a 16-day forecast in 1-day intervals")
    @GetMapping(WeatherEndpoint.DAILY_FORECAST)
    public ResponseEntity<List<DayForecastDto>> get16DaysDailyForecast() {
        return new ResponseEntity<>(weatherService.getDailyForecast(), HttpStatus.CREATED);
    }
}
