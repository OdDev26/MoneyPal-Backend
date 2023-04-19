package com.user__.controller;


import com.user__.form_error_handler.MapValidationErrorService;
import com.user__.request.WalletCreationRequest;
import com.user__.request.WalletFundingRequest;
import com.user__.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/wallet")
    public ResponseEntity<?> createWallet(@Valid @RequestBody WalletCreationRequest walletCreationRequest, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.MapValidationService(result);
        if(responseEntity!=null) return responseEntity;
        return walletService.createWallet(walletCreationRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/fund/wallet")
    public ResponseEntity<?> fundWallet(@RequestBody WalletFundingRequest fundingRequest){
        return walletService.fundWallet(fundingRequest);
    }
}

