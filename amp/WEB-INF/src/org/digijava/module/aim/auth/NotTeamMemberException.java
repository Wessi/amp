package org.digijava.module.aim.auth;

import org.springframework.security.AuthenticationException;

public class NotTeamMemberException
    extends AuthenticationException {

    public NotTeamMemberException(String email) {
        super("User " + email + " is not part of any team");
    }
}
