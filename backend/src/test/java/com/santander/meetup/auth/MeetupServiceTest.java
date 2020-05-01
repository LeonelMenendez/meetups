package com.santander.meetup.auth;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.endpoint.MeetupEndpoint;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.service.InvitationService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import com.santander.meetup.service.implementation.MeetupServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
public class MeetupServiceTest {

    private MeetupService meetupService;

    @Mock
    private MeetupRepository meetupRepository;

    @Mock
    private UserService userService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        meetupService = new MeetupServiceImpl(meetupRepository, userService, invitationService, modelMapper);
    }

    @Test
    public void getNeededBeerCases_19temperatureAnd0Participants_shouldReturn0() throws Exception {
        assertEquals(0, meetupService.calculateNeededBeerCases(19, 0));
    }

    @Test
    public void getNeededBeerCases_19temperatureAnd7Participants_shouldReturn1() throws Exception {
        assertEquals(1, meetupService.calculateNeededBeerCases(19, 7));
    }

    @Test
    public void getNeededBeerCases_19temperatureAnd8Participants_shouldReturn1() throws Exception {
        assertEquals(1, meetupService.calculateNeededBeerCases(19, 8));
    }

    @Test
    public void getNeededBeerCases_19temperatureAnd9Participants_shouldReturn2() throws Exception {
        assertEquals(2, meetupService.calculateNeededBeerCases(19, 9));
    }

    @Test
    public void getNeededBeerCases_20temperatureAnd0Participants_shouldReturn0() throws Exception {
        assertEquals(0, meetupService.calculateNeededBeerCases(20, 0));
    }

    @Test
    public void getNeededBeerCases_20temperatureAnd5Participants_shouldReturn1() throws Exception {
        assertEquals(1, meetupService.calculateNeededBeerCases(20, 5));
    }

    @Test
    public void getNeededBeerCases_24temperatureAnd0Participants_shouldReturn0() throws Exception {
        assertEquals(0, meetupService.calculateNeededBeerCases(24, 0));
    }

    @Test
    public void getNeededBeerCases_24temperatureAnd7Participants_shouldReturn2() throws Exception {
        assertEquals(2, meetupService.calculateNeededBeerCases(24, 7));
    }

    @Test
    public void getNeededBeerCases_25temperatureAnd0Participants_shouldReturn0() throws Exception {
        assertEquals(0, meetupService.calculateNeededBeerCases(25, 0));
    }

    @Test
    public void getNeededBeerCases_25temperatureAnd5Participants_shouldReturn2() throws Exception {
        assertEquals(2, meetupService.calculateNeededBeerCases(25, 5));
    }

    @Test
    public void getNeededBeerCases_25temperatureAnd6Participants_shouldReturn2() throws Exception {
        assertEquals(2, meetupService.calculateNeededBeerCases(25, 6));
    }

    @Test
    public void getNeededBeerCases_25temperatureAnd7Participants_shouldReturn3() throws Exception {
        assertEquals(3, meetupService.calculateNeededBeerCases(25, 7));
    }
}