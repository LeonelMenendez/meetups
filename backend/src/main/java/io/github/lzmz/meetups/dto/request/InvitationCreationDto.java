package io.github.lzmz.meetups.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class InvitationCreationDto implements Serializable {

    @NotNull
    @Schema(required = true)
    private Long meetupId;

    @NotNull
    @Schema(required = true)
    private Long userId;
}
