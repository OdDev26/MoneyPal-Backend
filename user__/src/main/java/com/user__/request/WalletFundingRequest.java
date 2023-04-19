package com.user__.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletFundingRequest {
    private String walletName;
    private Double amount;
}
