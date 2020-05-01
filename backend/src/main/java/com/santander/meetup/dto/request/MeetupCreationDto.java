package com.santander.meetup.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetupCreationDto implements Serializable {

    @NotNull
    @ApiModelProperty(required = true)
    private LocalDate day;

    @NotNull
    @ApiModelProperty(required = true)
    private Long ownerId;

    @NotNull
    @ApiModelProperty(required = true)
    private Double temperature;
}
