package com.user__.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class RequestService {
    @NotBlank(message = "Email is required")
    private String email;
    private String serviceName;
}
