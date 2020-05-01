package com.santander.meetup.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentCreationDto implements Serializable {

    @NotNull
    @ApiModelProperty(required = true)
    private Long meetupId;

    @NotNull
    @ApiModelProperty(required = true)
    private Long userId;
}
