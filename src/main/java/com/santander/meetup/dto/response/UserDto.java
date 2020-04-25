package com.santander.meetup.dto.response;

import com.santander.meetup.security.Role;
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
