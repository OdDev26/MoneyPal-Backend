package com.user__.exception;

import lombok.Data;

@Data
public class InvalidLoginResponse {
    private String userName;
    private String password;

    public InvalidLoginResponse(){
        this.userName= "Invalid username";
        this.password="Invalid password";
    }
}
