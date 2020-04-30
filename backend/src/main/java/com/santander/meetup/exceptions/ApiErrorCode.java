package com.santander.meetup.exceptions;

public class ApiErrorCode {
    public static int INTERNAL = 0;
    public static int REQUEST_METHOD_NOT_SUPPORTED = 20;
    public static int MEDIA_TYPE_NOT_SUPPORTED = 40;
    public static int METHOD_ARGUMENT_NOT_VALID = 60;
    public static int MISSING_SERVLET_REQUEST_PARAMETER = 80;
    public static int MESSAGE_NOT_READABLE = 100;
    public static int CONSTRAINT_VIOLATION = 120;
    public static int METHOD_ARGUMENT_TYPE_MISMATCH = 140;
    public static int BAD_CREDENTIALS = 160;
    public static int ENTITY_NOT_FOUND = 180;
    public static int DUPLICATE_ENTITY = 181;
    public static int VALUE_NOT_ALLOWED = 182;
    public static int JWT_UNEXPECTED = 200;
    public static int JWT_EXPIRED = 201;
    public static int JWT_UNSUPPORTED = 202;
    public static int JWT_MALFORMED = 203;
    public static int ACCESS_DENIED = 204;
}
