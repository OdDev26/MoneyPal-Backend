package com.user__.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class TransferRequest {
//    @NotBlank(message = "Amount is required")
    private Double amount;
    @NotBlank(message = "Your wallet name is required")
    private String senderWalletName;
    @NotBlank(message = "Receiver wallet name is required")
    private String receiverWalletName;
    @NotBlank(message = "Invoice no is required")
    private String invoiceNo;
}
