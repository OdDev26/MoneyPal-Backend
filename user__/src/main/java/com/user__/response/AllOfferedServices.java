package com.user__.response;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class AllOfferedServices {
    private String name;
    private String provider;

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("Odamic26."));
    }
}
