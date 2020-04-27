package com.santander.meetup.endpoint;

public class InvitationEndpoint {
    public static final String ROOT = "/invitations";
    public static final String ANT_ROOT = "/invitations*";
    public static final String INVITATION = "/{invitationId}";
    public static final String ANT_INVITATION = "/{invitationId:\\d+}";

}
