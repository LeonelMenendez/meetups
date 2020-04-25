package com.santander.meetup.constant;

public class EnrollmentEndPoint {
    public static final String ROOT = "/enrollments";
    public static final String CHECK_IN = "/{enrollmentId}/check-in";
    public static final String ANT_CHECK_IN = "/{enrollmentId:\\d+}/check-in";

}
