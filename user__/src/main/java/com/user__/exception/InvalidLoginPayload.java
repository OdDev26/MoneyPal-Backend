package com.user__.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvalidLoginPayload {
    private String username;
    private String password;
}
