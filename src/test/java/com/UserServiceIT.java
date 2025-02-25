package com;

import com.dto.request.UserLoginDto;
import com.exception.UserAlreadyExistsException;
import com.repository.UserRepository;
import com.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
@ActiveProfiles("integration ")
public class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        UserLoginDto user = new UserLoginDto("testUser", "password123");
        userService.createUser(user);

    }

    @Test
    public void testUserRegistration() {
        assertTrue(userRepository.findByLogin("testUser").isPresent());
    }


    @Test
    public void registerUserWithNotUniqueLoginShowThrowException() {
        UserLoginDto user = new UserLoginDto("testUser", "password123");
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(user);
        });
    }


}