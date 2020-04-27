package com.santander.meetup.dto.response;

import com.santander.meetup.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InvitationDto implements Serializable {

    private long meetupId;
    private long userId;
    private InvitationModel.Status status;
}
