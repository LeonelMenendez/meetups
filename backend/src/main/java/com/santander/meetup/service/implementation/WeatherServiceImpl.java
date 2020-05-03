package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.response.DailyForecastDto;
import com.santander.meetup.dto.response.DayForecastDto;
import com.santander.meetup.endpoint.WeatherConsumerEndpoint;
import com.santander.meetup.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WebClient webClient;

    @Value("${weatherbit.api-key}")
    private String apiKey;

    public WeatherServiceImpl() {
        this.webClient = WebClient.builder()
                .baseUrl(WeatherConsumerEndpoint.BASE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, WeatherConsumerEndpoint.CONTENT_TYPE)
                .build();
    }

    @Cacheable("${daily-forecast.cache-name}")
    @Override
    public List<DayForecastDto> getDailyForecast() {
        DailyForecastDto dailyForecast = webClient
                .get()
                .uri(builder -> builder.path(WeatherConsumerEndpoint.DAILY_FORECAST)
                        .queryParam("city", WeatherConsumerEndpoint.DEFAULT_CITY)
                        .queryParam("units", WeatherConsumerEndpoint.DEFAULT_METRIC_UNITS)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(DailyForecastDto.class)
                .block();


        return dailyForecast.getDayForecastDtos();
    }

    @Scheduled(cron = "${daily-forecast.cache-reset-expression}")
    @CacheEvict(value = "${daily-forecast.cache-name}")
    public void clearDailyForecastCache() {
    }
}
