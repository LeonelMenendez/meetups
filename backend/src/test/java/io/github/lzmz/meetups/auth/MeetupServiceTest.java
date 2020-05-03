package io.github.lzmz.meetups.auth;

import io.github.lzmz.meetups.dto.mapper.MeetupMapper;
import io.github.lzmz.meetups.repository.MeetupRepository;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.service.MeetupService;
import io.github.lzmz.meetups.service.implementation.MeetupServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class MeetupServiceTest {

    private MeetupService meetupService;

    @Mock
    private MeetupRepository meetupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MeetupMapper meetupMapper;

    @Before
    public void setUp() {
        meetupService = new MeetupServiceImpl(meetupRepository, userRepository, meetupMapper);
    }

    @Test
    public void calculateNeededBeerCases_19temperatureAnd0Participants_shouldReturn0() {
        assertEquals(0, meetupService.calculateBeerCasesNeeded(19, 0));
    }

    @Test
    public void calculateNeededBeerCases_19temperatureAnd7Participants_shouldReturn1() {
        assertEquals(1, meetupService.calculateBeerCasesNeeded(19, 7));
    }

    @Test
    public void calculateNeededBeerCases_19temperatureAnd8Participants_shouldReturn1() {
        assertEquals(1, meetupService.calculateBeerCasesNeeded(19, 8));
    }

    @Test
    public void calculateNeededBeerCases_19temperatureAnd9Participants_shouldReturn2() {
        assertEquals(2, meetupService.calculateBeerCasesNeeded(19, 9));
    }

    @Test
    public void calculateNeededBeerCases_20temperatureAnd0Participants_shouldReturn0() {
        assertEquals(0, meetupService.calculateBeerCasesNeeded(20, 0));
    }

    @Test
    public void calculateNeededBeerCases_20temperatureAnd5Participants_shouldReturn1() {
        assertEquals(1, meetupService.calculateBeerCasesNeeded(20, 5));
    }

    @Test
    public void calculateNeededBeerCases_24temperatureAnd0Participants_shouldReturn0() {
        assertEquals(0, meetupService.calculateBeerCasesNeeded(24, 0));
    }

    @Test
    public void calculateNeededBeerCases_24temperatureAnd7Participants_shouldReturn2() {
        assertEquals(2, meetupService.calculateBeerCasesNeeded(24, 7));
    }

    @Test
    public void calculateNeededBeerCases_25temperatureAnd0Participants_shouldReturn0() {
        assertEquals(0, meetupService.calculateBeerCasesNeeded(25, 0));
    }

    @Test
    public void calculateNeededBeerCases_25temperatureAnd5Participants_shouldReturn2() {
        assertEquals(2, meetupService.calculateBeerCasesNeeded(25, 5));
    }

    @Test
    public void calculateNeededBeerCases_25temperatureAnd6Participants_shouldReturn2() {
        assertEquals(2, meetupService.calculateBeerCasesNeeded(25, 6));
    }

    @Test
    public void calculateNeededBeerCases_25temperatureAnd7Participants_shouldReturn3() {
        assertEquals(3, meetupService.calculateBeerCasesNeeded(25, 7));
    }
}