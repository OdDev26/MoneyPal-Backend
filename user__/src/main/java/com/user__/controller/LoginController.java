package com.user__.controller;



import com.user__.exception.GenericMessage;
import com.user__.form_error_handler.MapValidationErrorService;
import com.user__.jwt.JwtAuthResponse;
import com.user__.jwt.JwtTokenProvider;
import com.user__.request.LoginRequest;
import com.user__.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class LoginController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MapValidationErrorService errorService;

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) throws Exception {
        ResponseEntity<?> responseEntity = errorService.MapValidationService(result);
        if(responseEntity!=null) return responseEntity;
        boolean userEmailHasBeenValidated = userService.checkIfUserEmailHasBeenValidated(loginRequest.getUsernameOrEmail());
        if (userEmailHasBeenValidated) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(true, token);
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        }

        GenericMessage genericMessage= new GenericMessage();
        genericMessage.setMessage("Pls send the link in your email to validate your email");

        return new ResponseEntity<>(genericMessage, HttpStatus.NOT_ACCEPTABLE);

    }
}
