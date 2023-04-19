package com.user__.form_error_handler;

import com.user__.entity.User;
import com.user__.repository.UserRepository;
import com.user__.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationRequestValidator implements Validator {
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean supports(Class<?> clazz) {
       return RegistrationRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
      RegistrationRequest registrationRequest= (RegistrationRequest) target;

      if(registrationRequest.getUserName().length()<6){
          errors.rejectValue("userName","Length","User name must be at least 6 characters");
      }
      if(registrationRequest.getPassword().length()<6){
          errors.rejectValue("password","Length","Password must be at least 6 characters");
      }
        if(!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())){
            errors.rejectValue("confirmPassword","Match","Passwords must match");
        }
        User user= userRepository.findByPhoneNumber(registrationRequest.getPhoneNumber());
        if(user!=null){
            errors.rejectValue("phoneNumber","Exists","User exists with phone no");
        }
        if(registrationRequest.getDateOfBirth()==null){
            errors.rejectValue("dateOfBirth","Null","Date of birth is required");
        }
        User user1=userRepository.findUserByEmailOrUsername(registrationRequest.getUserName());
        if(user1!=null){
            errors.rejectValue("userName","Exists","User with this username already exists");
        }
    }

}
