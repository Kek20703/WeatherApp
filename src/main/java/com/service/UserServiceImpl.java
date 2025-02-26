package com.service;

import com.dto.request.UserLoginDto;
import com.entity.User;
import com.entity.UserSession;
import com.exception.InvalidUserSession;
import com.exception.UserAlreadyExistsException;
import com.repository.UserRepository;
import com.util.passwordutil.PasswordUtil;
import lombok.RequiredArgsConstructor;
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
        UserSession userSession = userSessionService.getSession(user.get());
        return Optional.of(userSession);
    }

    @Override
    public void logout(UUID sessionId) {
        userSessionService.deleteSessionById(sessionId);
    }

    @Override
    public User getUser(UUID sessionId) {
        Optional<UserSession> session = userSessionService.findById(sessionId);
        if (session.isPresent()) {
            return session.get().getUser();
        } else {

            throw new InvalidUserSession("Current Session is invalid");
        }

    }


}
