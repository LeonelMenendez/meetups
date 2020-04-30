package com.santander.meetup.controller;

import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.dto.response.MeetupAdminDto;
import com.santander.meetup.dto.response.MeetupUserDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.endpoint.UserEndpoint;
import com.santander.meetup.security.Role;
import com.santander.meetup.service.EnrollmentService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Users")
@RestController
@RequestMapping(value = UserEndpoint.ROOT)
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MeetupService meetupService;

    @Autowired
    EnrollmentService enrollmentService;

    /**
     * Finds all the users filtered by the given parameters.
     *
     * @param role the role that will be filtered.
     * @return a list of users filtered by the given parameters.
     */
    @ApiOperation(value = "Retrieves a list of users filtered by the given parameters")
    @GetMapping()
    public ResponseEntity<List<UserDto>> findAll(@Valid @RequestParam(required = false) Role role) {
        return new ResponseEntity<>(userService.findAll(role), HttpStatus.OK);
    }

    /**
     * Retrieves the user's enrollments.
     *
     * @param userId the user id of the enrollments to retrieve.
     * @return the user's enrollments.
     */
    @ApiOperation(value = "Retrieves the user's enrollments")
    @GetMapping(UserEndpoint.ENROLLMENTS)
    public ResponseEntity<List<EnrollmentDto>> findAll(@Valid @PathVariable long userId) {
        return new ResponseEntity<>(enrollmentService.findAll(userId), HttpStatus.OK);
    }

    /**
     * Retrieves the meetups created by the given user.
     *
     * @param userId the owner id of the meetups to retrieve.
     * @return a list of the meetups created by the given user.
     */
    @ApiOperation(value = "Retrieves the meetups created by the given user")
    @GetMapping(UserEndpoint.MEETUPS_CREATED)
    public ResponseEntity<List<MeetupAdminDto>> getCreatedMeetups(@Valid @PathVariable long userId) {
        return new ResponseEntity<>(meetupService.getCreatedMeetups(userId), HttpStatus.OK);
    }

    /**
     * Retrieves the meetups in which the given user is enrolled.
     *
     * @param userId the user id of the meetups to retrieve.
     * @return a list of meetups in which the given user is enrolled.
     */
    @ApiOperation(value = "Retrieves the meetups in which the given user is enrolled")
    @GetMapping(UserEndpoint.MEETUPS_ENROLLED)
    public ResponseEntity<List<MeetupUserDto>> getEnrolledMeetups(@Valid @PathVariable long userId) {
        return new ResponseEntity<>(meetupService.getEnrolledMeetups(userId), HttpStatus.OK);
    }
}
