package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.config.cache.WeatherCache;
import io.github.lzmz.meetups.config.properties.ApiProperties;
import io.github.lzmz.meetups.dto.response.DailyForecastDto;
import io.github.lzmz.meetups.dto.response.DayForecastDto;
import io.github.lzmz.meetups.endpoint.WeatherConsumerEndpoint;
import io.github.lzmz.meetups.service.WeatherService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WebClient webClient;
    private final ApiProperties apiProperties;

    public WeatherServiceImpl(WebClient webClient, ApiProperties apiProperties) {
        this.webClient = webClient;
        this.apiProperties = apiProperties;

    }

    @Cacheable(value = WeatherCache.DAILY_FORECAST_NAME)
    @Override
    public List<DayForecastDto> getDailyForecast() {
        return this.webClient
                .get()
                .uri(builder -> builder
                        .path(WeatherConsumerEndpoint.DAILY_FORECAST)
                        .queryParam("city", WeatherConsumerEndpoint.DEFAULT_CITY)
                        .queryParam("units", WeatherConsumerEndpoint.DEFAULT_METRIC_UNITS)
                        .queryParam("key", apiProperties.getWeatherbit().getApiKey())
                        .build())
                .retrieve()
                .bodyToMono(DailyForecastDto.class)
                .block()
                .getDayForecastDtos();
    }

    @Scheduled(cron = WeatherCache.DAILY_FORECAST_EVICT_EXPRESSION)
    @CacheEvict(value = WeatherCache.DAILY_FORECAST_NAME)
    public void clearDailyForecastCache() {
    }
}
