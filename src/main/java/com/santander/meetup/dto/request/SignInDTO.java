package com.santander.meetup.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignInDTO {

    @NotBlank
    @Email
    @ApiModelProperty(required = true)
    private String email;

    @NotBlank
    @ApiModelProperty(required = true)
    private String password;
}
