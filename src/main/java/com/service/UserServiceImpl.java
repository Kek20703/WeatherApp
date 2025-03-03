package com.service;

import com.dto.request.UserLoginDto;
import com.entity.User;
import com.entity.UserSession;
import com.exception.InvalidUserSession;
import com.exception.UserAlreadyExistsException;
import com.repository.UserRepository;
import com.util.passwordutil.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserSessionService userSessionService;
    private final UserRepository userRepository;

    @Override
    public void createUser(UserLoginDto credentials) {
        String hashPassword = PasswordUtil.hashPassword(credentials.password());
        try {
            userRepository.save(new User(credentials.username(), hashPassword));
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists");
        }

    }

    @Override
    public Optional<UserSession> login(UserLoginDto credentials) {
        return userRepository.findByLogin(credentials.username())
                .filter(user -> PasswordUtil.matches(credentials.password(), user.getPassword()))
                .map(userSessionService::getSessionByUser);
    }

    @Override
    public void logout(UUID sessionId) {
        userSessionService.deleteSessionById(sessionId);
    }

    @Override
    public User getUser(UUID sessionId) {
        return userSessionService.findById(sessionId)
                .map(UserSession::getUser)
                .orElseThrow(() -> new InvalidUserSession("Current Session is invalid"));
    }

}
