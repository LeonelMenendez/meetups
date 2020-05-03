package io.github.lzmz.meetups.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DailyForecastDto implements Serializable {

    @JsonAlias("data")
    List<DayForecastDto> dayForecastDtos;
}
