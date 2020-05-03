package io.github.lzmz.meetups.dto.response;

import io.github.lzmz.meetups.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class InvitationDto implements Serializable {

    private long id;
    private long userId;
    private long meetupId;
    private String meetupOwnerName;
    private String meetupOwnerEmail;
    private LocalDate meetupDay;
    private double meetupTemperature;
    private InvitationModel.Status status;
}
