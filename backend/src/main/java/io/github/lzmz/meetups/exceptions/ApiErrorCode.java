package io.github.lzmz.meetups.exceptions;

public class ApiErrorCode {
    public static int INTERNAL = 0;
    public static int NOT_FOUND = 20;
    public static int UNSUPPORTED_MEDIA_TYPE = 40;
    public static int METHOD_NOT_ALLOWED = 41;
    public static int METHOD_ARGUMENT_NOT_VALID = 42;
    public static int METHOD_ARGUMENT_TYPE_MISMATCH = 43;
    public static int MESSAGE_NOT_READABLE = 60;
    public static int MISSING_SERVLET_REQUEST_PARAMETER = 61;
    public static int CONSTRAINT_VIOLATION = 80;
    public static int ENTITY_NOT_FOUND = 100;
    public static int DUPLICATE_ENTITY = 101;
    public static int VALUE_NOT_ALLOWED = 102;
    public static int BAD_CREDENTIALS = 120;
    public static int JWT_UNEXPECTED = 121;
    public static int JWT_EXPIRED = 122;
    public static int JWT_UNSUPPORTED = 123;
    public static int JWT_MALFORMED = 124;
    public static int ACCESS_DENIED = 125;
}
