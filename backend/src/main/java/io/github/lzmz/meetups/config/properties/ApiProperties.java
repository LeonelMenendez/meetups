package io.github.lzmz.meetups.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiProperties {

    /**
     * CORS properties.
     */
    private Cors cors = new Cors();

    /**
     * Weatherbit properties.
     */
    private Weatherbit weatherbit = new Weatherbit();

    @Getter
    @Setter
    public static class Cors {

        /**
         * CORS origin to allow.
         */
        private String allowedOrigin = "";
    }

    @Getter
    @Setter
    public static class Weatherbit {

        /**
         * Weatherbit API key.
         */
        private String apiKey = "";
    }

}
