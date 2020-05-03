package io.github.lzmz.meetups.endpoint;

public class EnrollmentEndpoint {
    public static final String BASE = "/enrollments";
    public static final String CHECK_IN = "/{enrollmentId}/check-in";
    public static final String ANT_CHECK_IN = "/{enrollmentId:\\d+}/check-in";

}
