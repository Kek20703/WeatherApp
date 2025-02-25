package com;

import com.entity.User;
import com.entity.UserSession;
import com.repository.UserRepository;
import com.repository.UserSessionRepository;
import com.scheduller.SessionCleanupScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("integration ")
public class SessionCleanupSchedullerIT {

    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private SessionCleanupScheduler sessionCleanupScheduler;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void expiredSessionsShouldBeCleanedUp() {
        User user = new User("Name", "password");
        UserSession expiredSession = new UserSession(user,
                LocalDateTime.now().minusDays(140));
        expiredSession.setId(UUID.randomUUID());
        userRepository.save(user);
        userSessionRepository.save(expiredSession);

        sessionCleanupScheduler.cleanupExpiredSessions();
        userSessionRepository.findById(expiredSession.getId());
        Optional<UserSession> userSessionOptional = userSessionRepository.findById(expiredSession.getId());

        assertFalse(userSessionOptional.isPresent(), "Expired session should be deleted after cleanup");
    }

    @Test
    public void validSessionsShouldNotBeCleanedUp() {
        User user = new User("Name", "password");
        UserSession expiredSession = new UserSession(user,
                LocalDateTime.now().plusDays(140));
        expiredSession.setId(UUID.randomUUID());
        userRepository.save(user);
        userSessionRepository.save(expiredSession);

        sessionCleanupScheduler.cleanupExpiredSessions();
        userSessionRepository.findById(expiredSession.getId());
        Optional<UserSession> userSessionOptional = userSessionRepository.findById(expiredSession.getId());

        assertTrue(userSessionOptional.isPresent(), "ValidSessionsShouldNotBeCleanedUp");
    }
}
