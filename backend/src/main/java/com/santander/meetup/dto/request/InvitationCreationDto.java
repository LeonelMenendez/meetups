package com.santander.meetup.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class InvitationCreationDto implements Serializable {

    @NotNull
    @ApiModelProperty(required = true)
    private Long meetupId;

    @NotNull
    @ApiModelProperty(required = true)
    private Long userId;
}
