package com.user__.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WalletCreationRequest {
    @NotBlank(message = "Wallet name is required")
    private String walletName;
    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
