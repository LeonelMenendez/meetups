package com.santander.meetup.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class MeetupDto implements Serializable {

    private long id;
    private LocalDate day;
    private long ownerId;
    private double temperature;
    private int beerCasesNeeded;
}
