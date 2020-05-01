package com.santander.meetup.endpoint;

public class InvitationEndpoint {
    public static final String BASE = "/invitations";
    public static final String ANT_BASE = "/invitations*";
    public static final String INVITATION = "/{invitationId}";
    public static final String ANT_INVITATION = "/{invitationId:\\d+}";

}
