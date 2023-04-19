package com.user__.service;




import com.user__.entity.Service;
import com.user__.request.OfferService;
import com.user__.request.RegistrationRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
   ResponseEntity<?> registerUser(RegistrationRequest registrationRequest);

   String offerService(OfferService offerService);

   List<Service> viewAllServicesThatCanBeOfferedAndRequested();

   ResponseEntity<?> getWalletDetails(String email,String username);

   ResponseEntity<?> getWalletDetails1(String usernameOrEmail);

   ResponseEntity<?> getWalletDetails2(String usernameOrEmail);

   Service viewAllServicesById(Long id);

 ResponseEntity<?> validateToken(String token);

   boolean  checkIfUserEmailHasBeenValidated(String username);

//   List<ServicesOffered> fetchServicesOffered();
}
