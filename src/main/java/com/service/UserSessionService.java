package com.service;

import com.entity.User;
import com.entity.UserSession;

import java.util.UUID;

public interface UserSessionService {
    UserSession getSessionByUser(User user);

    void deleteSessionById(UUID sessionId);

    UserSession getById(UUID userId);

    boolean isSessionValid(UUID sessionId);

}
