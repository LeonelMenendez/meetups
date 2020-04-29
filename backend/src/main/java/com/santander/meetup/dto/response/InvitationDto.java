package com.santander.meetup.dto.response;

import com.santander.meetup.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class InvitationDto implements Serializable {

    private long id;
    private long meetupId;
    private long userId;
    private String meetupOwnerName;
    private String meetupOwnerEmail;
    private LocalDate meetupDay;
    private double meetupTemperature;
    private InvitationModel.Status status;
}
