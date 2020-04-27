package com.santander.meetup.dto.request;

import com.santander.meetup.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InvitationPatchDto implements Serializable {

    private InvitationModel.Status status;
}
