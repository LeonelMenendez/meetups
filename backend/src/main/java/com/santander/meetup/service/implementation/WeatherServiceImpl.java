package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.response.DailyForecastDto;
import com.santander.meetup.dto.response.DayForecastDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.security.JwtUtil;
import com.santander.meetup.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${weatherbit.api-key}")
    private String apiKey;

    private final WebClient webClient;
    private static final String BASE_URL = "http://api.weatherbit.io/v2.0";
    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    private static final String END_POINT_DAILY_FORECAST = "/forecast/daily";
    private static final String CITY = "Buenos Aires,AR";
    private static final String METRIC_UNITS = "M";

    private final static String DAILY_FORECAST_CACHE_NAME = "dailyForecast";
    private final static String DAILY_FORECAST_CACHE_RESET_EXPRESSION = "0 0 0 * * *";

    public WeatherServiceImpl() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
                .build();
    }

    @Cacheable(DAILY_FORECAST_CACHE_NAME)
    @Override
    public List<DayForecastDto> getDailyForecast() {
        DailyForecastDto dailyForecast = webClient
                .get()
                .uri(builder -> builder.path(END_POINT_DAILY_FORECAST)
                        .queryParam("city", CITY)
                        .queryParam("days", 16)
                        .queryParam("units", METRIC_UNITS)
                        .queryParam("key", "apiKey")
                        .build())
                .retrieve()
                .bodyToMono(DailyForecastDto.class)
                .block();


        return dailyForecast.getDayForecastDtos();
    }

    @Scheduled(cron = DAILY_FORECAST_CACHE_RESET_EXPRESSION)
    @CacheEvict(value = DAILY_FORECAST_CACHE_NAME)
    public void clearDailyForecastCache() {
    }
}
