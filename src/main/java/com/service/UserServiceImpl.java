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
            // TODO: вроде нет уникальности на login, этой ошибки не должно быть
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    // TODO: первым запросом идем в базу и ищем пользователя, вторым запросом по пользователю ищем его сессию, если
    //  сессии нет - третьим запросом создаем новую сессию. Можно ли сократить кол-во операций?
    @Override
    public Optional<UserSession> login(UserLoginDto credentials) {
        /* TODO: можно сделать за один запрос в бд по username и hashed password (не особо важно, но нужно это понимать)
        *   так как нет unique constraint на login, нет гарантий, что в бд лежит один пользователь
        *   что в таком случае вернет репозиторий? список или первого попавшегося - в любом случае выглядит как ошибка
        */
        return userRepository.findByLogin(credentials.username())
                .filter(user -> PasswordUtil.matches(credentials.password(), user.getPassword()))
                .map(userSessionService::getSession);
    }

    // TODO: как будто можно обойтись без этого метода в данном классе, так как сущность юзера никак не затрагивается
    @Override
    public void logout(UUID sessionId) {
        userSessionService.deleteSessionById(sessionId);
    }


    // TODO: метод работает с сессией, как будто его тут не должно быть
    @Override
    public User getUser(UUID sessionId) {
        return userSessionService.findById(sessionId)
                .map(UserSession::getUser)
                .orElseThrow(() -> new InvalidUserSession("Current Session is invalid"));
    }

}
