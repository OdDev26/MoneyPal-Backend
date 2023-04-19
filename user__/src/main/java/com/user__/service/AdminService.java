package com.user__.service;


import com.user__.request.CreateService;
import com.user__.request.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<?> registerAdmin(RegistrationRequest registrationRequest);
    ResponseEntity<?> addService(CreateService createService);

}
