package io.github.lzmz.meetups.exceptions;

public final class ApiErrorCode {
    public static final int INTERNAL = 0;
    public static final int NOT_FOUND = 20;
    public static final int UNSUPPORTED_MEDIA_TYPE = 40;
    public static final int METHOD_NOT_ALLOWED = 41;
    public static final int METHOD_ARGUMENT_NOT_VALID = 42;
    public static final int METHOD_ARGUMENT_TYPE_MISMATCH = 43;
    public static final int MESSAGE_NOT_READABLE = 60;
    public static final int MISSING_SERVLET_REQUEST_PARAMETER = 61;
    public static final int CONSTRAINT_VIOLATION = 80;
    public static final int ENTITY_NOT_FOUND = 100;
    public static final int DUPLICATE_ENTITY = 101;
    public static final int VALUE_NOT_ALLOWED = 102;
    public static final int BAD_CREDENTIALS = 120;
    public static final int ACCESS_DENIED = 121;
    public static final int JWT_UNSUPPORTED = 122;
    public static final int JWT_MALFORMED = 123;
    public static final int JWT_INVALID_SIGNATURE = 124;
    public static final int JWT_EXPIRED = 125;
    public static final int JWT_ILLEGAL_ARGUMENT = 126;
    public static final int JWT_UNEXPECTED = 127;
}
