package io.github.lzmz.meetups.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class EnrollmentDto implements Serializable {

    private long id;
    private long userId;
    private long meetupId;
    private String meetupOwnerName;
    private String meetupOwnerEmail;
    private LocalDate meetupDay;
    private double meetupTemperature;
    private boolean checkedIn;
}
