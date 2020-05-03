package io.github.lzmz.meetups.dto.request;

import io.github.lzmz.meetups.model.InvitationModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InvitationStatusDto implements Serializable {

    private InvitationModel.Status status;
}
