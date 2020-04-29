package com.santander.meetup.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class MeetupUserDto implements Serializable {

    private long id;
    private LocalDate day;
    private long ownerId;
    private String ownerName;
    private String ownerEmail;
    private double temperature;
}
