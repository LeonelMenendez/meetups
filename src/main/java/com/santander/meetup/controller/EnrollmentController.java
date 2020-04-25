package com.santander.meetup.controller;

import com.santander.meetup.constant.EnrollmentEndPoint;
import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.service.EnrollmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Enrollments")
@RestController
@RequestMapping(value = EnrollmentEndPoint.ROOT)
public class EnrollmentController {

    @Autowired
    EnrollmentService enrollmentService;

    /**
     * Creates a new enrollment.
     *
     * @param enrollmentCreationDto the enrollment creation request body.
     * @return the created enrollment.
     * @throws DuplicateEntityException if the enrollment already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    @ApiOperation(value = "Creates a new enrollment")
    @PostMapping()
    public ResponseEntity<EnrollmentDto> create(@Valid @RequestBody EnrollmentCreationDto enrollmentCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        return new ResponseEntity<>(enrollmentService.create(enrollmentCreationDto), HttpStatus.CREATED);
    }

    /**
     * Makes the check-in of the user associated to the given enrollment.
     *
     * @param enrollmentId the enrollment id for which will be make the check-in.
     * @return the updated enrollment.
     * @throws EntityNotFoundException if the enrollment already exists.
     */
    @ApiOperation(value = "Makes the check-in of the user associated to the given enrollment")
    @PostMapping(EnrollmentEndPoint.CHECK_IN)
    public ResponseEntity<EnrollmentDto> create(@Valid @PathVariable long enrollmentId) throws EntityNotFoundException {
        return new ResponseEntity<>(enrollmentService.checkIn(enrollmentId), HttpStatus.CREATED);
    }
}
