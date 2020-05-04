package io.github.lzmz.meetups.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class DayForecastDto implements Serializable {

    @JsonAlias("datetime")
    private LocalDate day;

    @JsonAlias("temp")
    @Parameter(name = "Average Temperature")
    private double temperature;
}
