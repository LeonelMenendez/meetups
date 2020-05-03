package io.github.lzmz.meetups.dto.response;

import io.github.lzmz.meetups.security.Role;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    private long id;
    private String name;
    private String email;
    private Role role;
    private String token;
}
