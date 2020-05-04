package io.github.lzmz.meetups.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
public class SignUpDto implements Serializable {

    @NotBlank
    @Schema(required = true)
    private String name;

    @NotBlank
    @Email
    @Schema(required = true)
    private String email;

    @NotBlank
    @Schema(required = true)
    private String password;

    @Schema(defaultValue = "false")
    private boolean admin;
}