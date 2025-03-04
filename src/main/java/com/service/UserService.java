package com.service;

import com.dto.request.UserLoginDto;
import com.entity.User;
import com.entity.UserSession;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(UserLoginDto credentials);

    Optional<UserSession> login(UserLoginDto credentials);

    User getUser(UUID sessionId);
}
