package com.user__.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be blank")
    private String usernameOrEmail;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
