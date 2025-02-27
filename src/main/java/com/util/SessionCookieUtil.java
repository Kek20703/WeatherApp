package com.util;

import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionCookieUtil {
    private static final String COOKIE_NAME = "SESSIONID";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE_SECONDS = 86400;

    public static Cookie getNewSessionCookie(String sessionId) {
        Cookie sessionCookie = new Cookie(COOKIE_NAME, sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath(COOKIE_PATH);
        sessionCookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        return sessionCookie;
    }

    public static Cookie getEmptySessionCookie() {
        Cookie cookie = new Cookie(COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath(COOKIE_PATH);
        return cookie;
    }
}
