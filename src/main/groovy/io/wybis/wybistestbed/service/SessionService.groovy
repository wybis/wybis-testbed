package io.wybis.wybistestbed.service

import io.wybis.wybistestbed.dto.User

interface SessionService {

    boolean ping()

    boolean isLoggedIn()

    String getSessionId()

    boolean login()

    void execute()

    boolean logout()

}
