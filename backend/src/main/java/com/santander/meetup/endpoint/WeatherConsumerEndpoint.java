package com.santander.meetup.endpoint;

import org.springframework.http.MediaType;

public class WeatherConsumerEndpoint {
    public static final String BASE = "http://api.weatherbit.io/v2.0";
    public static final String DAILY_FORECAST = "/forecast/daily";
    public static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    public static final String DEFAULT_CITY = "Buenos Aires,AR";
    public static final String DEFAULT_METRIC_UNITS = "M";
}