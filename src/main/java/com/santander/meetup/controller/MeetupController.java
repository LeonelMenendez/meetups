package com.santander.meetup.controller;

import com.santander.meetup.constant.MeetupEndPoint;
import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.service.MeetupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Meetups")
@RestController
@RequestMapping(value = MeetupEndPoint.ROOT)
public class MeetupController {

    @Autowired
    MeetupService meetupService;

    /**
     * Creates a new meetup.
     *
     * @param meetupCreationDto the meetup creation request body.
     * @return the created meetup.
     * @throws DuplicateEntityException if the meetup already exists.
     */
    @ApiOperation(value = "Creates a new meetup")
    @PostMapping()
    public ResponseEntity<MeetupDto> create(@Valid @RequestBody MeetupCreationDto meetupCreationDto) throws DuplicateEntityException {
        return new ResponseEntity<>(meetupService.create(meetupCreationDto), HttpStatus.CREATED);
    }
}
