package com.service;

import com.entity.User;
import com.entity.UserSession;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionService {
  UserSession getSession(User user);
  void deleteSessionById(UUID sessionId);
  Optional<UserSession> findById(UUID sessionId);

}
