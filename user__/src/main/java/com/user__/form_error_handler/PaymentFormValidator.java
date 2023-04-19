package com.user__.form_error_handler;

import com.user__.request.TransferRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class PaymentFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TransferRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransferRequest transferRequest= (TransferRequest) target;

        if(transferRequest.getAmount()==null){
            errors.rejectValue("amount","blank","amount is required");

        }
    }
}
