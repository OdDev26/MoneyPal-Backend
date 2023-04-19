package com.user__.controller;



import com.fasterxml.jackson.core.JsonProcessingException;


import com.user__.entity.Service;
import com.user__.form_error_handler.MapValidationErrorService;
import com.user__.form_error_handler.PaymentFormValidator;
import com.user__.form_error_handler.RegistrationRequestValidator;
import com.user__.request.OfferService;
import com.user__.request.RegistrationRequest;
import com.user__.request.TransferRequest;

import com.user__.response.TransactionResponseDto;
import com.user__.service.PaymentService;
import com.user__.service.UserService;
import com.user__.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RegistrationRequestValidator registrationRequestValidator;

    @Autowired
    private MapValidationErrorService validationErrorService;

    @Autowired
    private PaymentFormValidator paymentFormValidator;

    @PostMapping("/auth/regular/user/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult result) {
        registrationRequestValidator.validate(registrationRequest, result);
        ResponseEntity<?> errorMap = validationErrorService.MapValidationService(result);
        if (errorMap != null) return errorMap;
        return userService.registerUser(registrationRequest);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/view/services")
    public List<Service> viewAllServicesAvailable() {

        return userService.viewAllServicesThatCanBeOfferedAndRequested();

    }

    @GetMapping("/user/view/services/{id}")
    public Service viewAllServicesById(@PathVariable ("id") Long id) {
        return userService.viewAllServicesById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/offer/service")
    public String offerService(@RequestBody OfferService offerService){
        return userService.offerService(offerService);
    }
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/get/wallet/details/{email}/{username}")
//    public ResponseEntity<?> retrieveWalletInfo(@PathVariable("email") String email, @PathVariable("username") String username) {
//        return userService.getWalletDetails(email, username);
//    }

    @GetMapping("/validate/email")
    public String validateEmail(@RequestParam("token") String token){
        userService.validateToken(token);
        return "Email confirmed";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/wallet/details/{usernameOrEmail}")
    public ResponseEntity<?> retrieveWalletInfo2(@PathVariable(value = "usernameOrEmail") String usernameOrEmail){
        return userService.getWalletDetails2(usernameOrEmail);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/pay/for/service")
    public ResponseEntity<?> payForService(@Valid @RequestBody TransferRequest transferRequest,BindingResult bindingResult) throws JsonProcessingException, SQLException, ClassNotFoundException {
        paymentFormValidator.validate(transferRequest,bindingResult);
        ResponseEntity<?> responseEntity = validationErrorService.MapValidationService(bindingResult);
        if(responseEntity!=null) return  responseEntity;
        return paymentService.payForAService(transferRequest);
    }



}
