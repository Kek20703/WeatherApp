package com.util.validator;

import com.dto.request.SignUpUserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpUserDTOValidator implements Validator {
    private static final String NAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).+$";

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpUserDto.class.equals(clazz);
    }

    // TODO: очень большой метод. Хорошей практикой является разделение на более маленькие методы в простом случае
    //  в идеале один валидатор должен делать одну валидацию (тут это будет оверкилом, так что разделения на методы достаточно)
    @Override
    public void validate(Object target, Errors errors) {
        SignUpUserDto dto = (SignUpUserDto) target;

        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty", "Username cannot be empty");
        } else if (dto.getUsername().length() < 3) {
//            errors.rejectValue("username", "username.tooShort", "Username must be at least 3 characters long");
        } else if (!dto.getUsername().matches(NAME_PATTERN)) {
//            errors.rejectValue("username", "username.invalid", "Username must contain only Latin letters and digits");
        }

        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty", "Password cannot be empty");
        } else if (dto.getPassword().length() < 8 || dto.getPassword().length() > 20) {
//            errors.rejectValue("password", "password.invalidLength", "Password must be between 8 and 20 characters long");
        } else if (!dto.getPassword().matches(PASSWORD_PATTERN)) {
//            errors.rejectValue("password", "password.invalidPattern", "Password must contain at least one letter and one digit");
        }

        if (dto.getPasswordConfirmation() == null || dto.getPasswordConfirmation().isEmpty()) {
            errors.rejectValue("passwordConfirmation", "passwordConfirmation.empty", "Password confirmation cannot be empty");
        } else if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "passwordConfirmation.notMatch", "Passwords do not match");
        }

        if (dto.getPasswordConfirmation() == null || dto.getPasswordConfirmation().isEmpty()) {
            errors.rejectValue("passwordConfirmation", "passwordConfirmation.empty", "Password confirmation cannot be empty");
        } else if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
            errors.rejectValue("passwordConfirmation", "passwordConfirmation.notMatch", "Passwords do not match");
        }
    }
}
