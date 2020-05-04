package io.github.lzmz.meetups.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class MeetupCreationDto implements Serializable {

    @NotNull
    @Schema(required = true)
    private LocalDate day;

    @NotNull
    @Schema(required = true)
    private Long ownerId;

    @NotNull
    @Schema(required = true)
    private Double temperature;
}
