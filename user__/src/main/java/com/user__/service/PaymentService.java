package com.user__.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.user__.request.TransferRequest;
import com.user__.response.TransactionResponseDto;
import org.springframework.http.ResponseEntity;

;import java.sql.SQLException;


public interface PaymentService{
    ResponseEntity<?>payForAService(TransferRequest transferRequest) throws JsonProcessingException, ClassNotFoundException, SQLException;

}
