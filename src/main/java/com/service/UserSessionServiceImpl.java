package com.service;

import com.entity.User;
import com.entity.UserSession;
import com.exception.InvalidUserSession;
import com.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {
    private static final int SESSION_LIFETIME_SECONDS = 86400;
    private final UserSessionRepository userSessionRepository;

    @Override
    public UserSession getSessionByUser(User user) {
        Optional<UserSession> session = userSessionRepository.findByUserAndExpiresAtAfter(user, LocalDateTime.now());
        return session.orElseGet(() -> createAndGetSession(user));
    }

    @Override
    public void deleteSessionById(UUID sessionId) {
        userSessionRepository.deleteById(sessionId);
    }


    @Override
    public UserSession getById(UUID userId) {
        return userSessionRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserSession("Session is not valid"));
    }

    @Override
    public boolean isSessionValid(UUID sessionId) {
        Optional<UserSession> session = userSessionRepository.findByIdAndExpiresAtAfter(sessionId, LocalDateTime.now());
        return session.isPresent();
    }

    private UserSession createAndGetSession(User user) {
        UserSession session = new UserSession();
        session.setUser(user);
        session.setId(UUID.randomUUID());
        LocalDateTime expires = LocalDateTime.now().plusSeconds(SESSION_LIFETIME_SECONDS);
        session.setExpiresAt(expires);
        userSessionRepository.save(session);
        return session;
    }
}
