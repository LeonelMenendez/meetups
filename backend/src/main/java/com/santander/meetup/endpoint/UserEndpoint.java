package com.santander.meetup.endpoint;

public class UserEndpoint {
    public static final String BASE = "/users";
    public static final String ANT_BASE = "/users*";
    public static final String ENROLLMENTS = "/{userId}/enrollments";
    public static final String ANT_MEETUPS_ENROLLMENTS = "/{userId:\\d+}/enrollments";
    public static final String MEETUPS_CREATED = "/{userId}/meetups/created";
    public static final String ANT_MEETUPS_CREATED = "/{userId:\\d+}/meetups/created";
    public static final String MEETUPS_ENROLLED = "/{userId}/meetups/enrolled";
    public static final String ANT_MEETUPS_ENROLLED = "/{userId:\\d+}/meetups/enrolled";
}
