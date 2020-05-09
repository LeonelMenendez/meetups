package io.github.lzmz.meetups.config;

import io.github.lzmz.meetups.endpoint.WeatherConsumerEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient weatherbitWebClient() {
        return WebClient.builder()
                .baseUrl(WeatherConsumerEndpoint.BASE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, WeatherConsumerEndpoint.CONTENT_TYPE)
                .build();
    }
}
