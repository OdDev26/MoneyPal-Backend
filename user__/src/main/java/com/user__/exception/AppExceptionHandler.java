package com.user__.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
   @ExceptionHandler
    public ResponseEntity<?> returnUsernameException(UsernameException ex){
      UsernameExceptionResponse usernameExceptionResponse = new UsernameExceptionResponse(ex.getMessage());
      return new ResponseEntity<>(usernameExceptionResponse, HttpStatus.BAD_REQUEST);
   }

    @ExceptionHandler
        public ResponseEntity<?> returnEmailException(EmailException ex){
        EmailExceptionResponse emailExceptionResponse= new EmailExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(emailExceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<?> returnInvalidLoginException(InvalidLoginException ex){
        InvalidLoginPayload invalidLoginPayload= new InvalidLoginPayload(ex.getMessage(), ex.getMessage());
        return new ResponseEntity<>(invalidLoginPayload, HttpStatus.BAD_REQUEST);
    }
}
