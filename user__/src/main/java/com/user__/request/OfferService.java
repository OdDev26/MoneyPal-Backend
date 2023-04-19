package com.user__.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class OfferService {
    @NotBlank(message = "Email is required")
    private String email;
    private String serviceName;
}
