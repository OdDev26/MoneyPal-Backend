package com.user__.service;


import com.user__.request.WalletCreationRequest;
import com.user__.request.WalletFundingRequest;
import org.springframework.http.ResponseEntity;

public interface WalletService {
    ResponseEntity<?> fundWallet(WalletFundingRequest fundingRequest);
    ResponseEntity<?> createWallet(WalletCreationRequest walletDetails);

}
