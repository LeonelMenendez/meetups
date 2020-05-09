package io.github.lzmz.meetups.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties(prefix = "jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

    /**
     * Secret that will be used to sign the JWT.
     */
    @NotEmpty
    private String secret;

    /**
     * JWT duration.
     */
    @NotNull
    @DurationUnit(ChronoUnit.HOURS)
    private Duration tokenDuration;
}

