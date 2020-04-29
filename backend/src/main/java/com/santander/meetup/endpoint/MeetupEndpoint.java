package com.santander.meetup.endpoint;

public class MeetupEndpoint {
    public static final String ROOT = "/meetups";
    public static final String BEER_CASES = "/{meetupId}/beer-cases";
    public static final String ANT_BEER_CASES = "/{meetupId:\\d+}/beer-cases";
    public static final String TEMPERATURE = "/{meetupId}/temperature";
    public static final String ANT_TEMPERATURE = "/{meetupId:\\d+}/temperature";
    public static final String INVITATIONS = "/{meetupId}/invitations";
    public static final String ANT_INVITATIONS = "/{meetupId:\\d+}/invitations";
    public static final String CREATED = "/created";
    public static final String ANT_CREATED = "/created*";
    public static final String ENROLLED = "/enrolled";
    public static final String ANT_ENROLLED = "/enrolled*";
}
