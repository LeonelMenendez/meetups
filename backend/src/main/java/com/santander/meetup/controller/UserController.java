package com.santander.meetup.controller;

import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.endpoint.UserEndpoint;
import com.santander.meetup.security.Role;
import com.santander.meetup.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
