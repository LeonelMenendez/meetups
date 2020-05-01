package com.santander.meetup.dto.request;

import com.santander.meetup.model.InvitationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationPatchDto implements Serializable {

    private InvitationModel.Status status;
}
