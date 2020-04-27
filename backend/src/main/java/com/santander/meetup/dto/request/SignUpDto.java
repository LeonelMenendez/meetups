package com.santander.meetup.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class SignUpDto implements Serializable {

    @NotBlank
    @ApiModelProperty(required = true)
    private String name;

    @NotBlank
    @Email
    @ApiModelProperty(required = true)
    private String email;

    @NotBlank
    @ApiModelProperty(required = true)
    private String password;

    @NotBlank
    @ApiModelProperty(required = true)
    private String confirmPassword;

    private boolean admin;
}