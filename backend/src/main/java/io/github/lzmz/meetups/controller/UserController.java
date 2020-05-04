package io.github.lzmz.meetups.controller;

import io.github.lzmz.meetups.dto.response.EnrollmentDto;
import io.github.lzmz.meetups.dto.response.MeetupAdminDto;
import io.github.lzmz.meetups.dto.response.MeetupUserDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.endpoint.UserEndpoint;
import io.github.lzmz.meetups.security.Role;
import io.github.lzmz.meetups.service.EnrollmentService;
import io.github.lzmz.meetups.service.MeetupService;
import io.github.lzmz.meetups.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping(value = UserEndpoint.BASE)
public class UserController {

    private final UserService userService;
    private final MeetupService meetupService;
    private final EnrollmentService enrollmentService;

    public UserController(UserService userService, MeetupService meetupService, EnrollmentService enrollmentService) {
        this.userService = userService;
        this.meetupService = meetupService;
        this.enrollmentService = enrollmentService;
    }

    /**
     * Finds all the users filtered by the given parameters.
     *
     * @param role the role that will be filtered.
     * @return a list of users filtered by the given parameters.
     */
    @Operation(summary = "Retrieves a list of users filtered by the given parameters")
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
    @Operation(summary = "Retrieves the user's enrollments")
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
    @Operation(summary = "Retrieves the meetups created by the given user")
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
    @Operation(summary = "Retrieves the meetups in which the given user is enrolled")
    @GetMapping(UserEndpoint.MEETUPS_ENROLLED)
    public ResponseEntity<List<MeetupUserDto>> getEnrolledMeetups(@Valid @PathVariable long userId) {
        return new ResponseEntity<>(meetupService.getEnrolledMeetups(userId), HttpStatus.OK);
    }
}
