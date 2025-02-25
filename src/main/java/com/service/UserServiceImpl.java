package com.service;

import com.dto.request.UserLoginDto;
import com.entity.User;
import com.entity.UserSession;
import com.exception.InvalidUserSession;
import com.exception.UserAlreadyExistsException;
import com.repository.UserRepository;
import com.repository.UserSessionRepository;
import com.util.passwordutil.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final int SESSION_LIFETIME_SECONDS = 86400;
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserSessionRepository userSessionRepository, UserRepository userRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserLoginDto credentials) {
        Optional<User> user = userRepository.findByLogin(credentials.username());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        } else {
            String hashPassword = PasswordUtil.hashPassword(credentials.password());
            userRepository.save(new User(credentials.username(), hashPassword));
        }
    }

    @Override
    public Optional<UserSession> login(UserLoginDto credentials) {
        Optional<User> user = userRepository.findByLogin(credentials.username());
        if (user.isEmpty()) {
            return Optional.empty();
        }
        if (!PasswordUtil.matches(credentials.password(), user.get().getPassword())) {
            return Optional.empty();
        }
        UserSession userSession = getSession(user.get());
        return Optional.of(userSession);
    }

    @Override
    public void logout(UUID sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    @Override
    public User getUser(UUID sessionId) {
        Optional<UserSession> session = userSessionRepository.findById(sessionId);
        if (session.isPresent()) {
            return session.get().getUser();
        } else {

            throw new InvalidUserSession("Current Session is invalid");
        }

    }

    private UserSession getSession(User user) {
        Optional<UserSession> session = userSessionRepository.findByUserAndExpiresAtAfter(user, LocalDateTime.now());
        return session.orElseGet(() -> createSession(user));
    }

    private UserSession createSession(User user) {
        UserSession session = new UserSession();
        session.setUser(user);
        session.setId(UUID.randomUUID());
        LocalDateTime expires = LocalDateTime.now().plusSeconds(SESSION_LIFETIME_SECONDS);
        session.setExpiresAt(expires);
        userSessionRepository.save(session);
        return session;///вынести в отдельный сервис
    }

}
