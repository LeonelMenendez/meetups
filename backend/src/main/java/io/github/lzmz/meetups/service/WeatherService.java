package io.github.lzmz.meetups.service;

import io.github.lzmz.meetups.dto.response.DayForecastDto;

import java.util.List;

public interface WeatherService {

    /**
     * Retrieves a 16-day forecast in 1-day intervals.
     *
     * @return a list composed by 16-day forecast.
     */
    List<DayForecastDto> getDailyForecast();
}