package com.user__.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FundingResponse implements Serializable {
    private String walletName;
    private Double balance;
    private String funderEmail;
    private Double fundingAmount;
}
