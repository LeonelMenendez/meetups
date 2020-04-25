package com.santander.meetup.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EnrollmentDto implements Serializable {

    private long meetupId;
    private long userId;
    private boolean checkedIn;
}
