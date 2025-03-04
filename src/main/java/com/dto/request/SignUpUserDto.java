package com.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserDto {

    @NotNull(message = "Field cannot be null.")
    @NotBlank(message = "Field cannot be empty or contain only spaces.")
    private String username;

    @NotNull(message = "Field cannot be null.")
    @NotBlank(message = "Field cannot be empty or contain only spaces.")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 25 characters")
    private String password;

    @NotNull(message = "Field cannot be null.")
    @NotBlank(message = "Field cannot be empty or contain only spaces.")
    private String passwordConfirmation;

}
