package com.user__.implementation;

import com.user__.entity.EmailConfirmationToken;
import com.user__.repository.EmailConfirmationServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConfirmationServiceImpl {
    @Autowired
    private EmailConfirmationServiceRepository confirmationServiceRepository;

    public void saveConfirmationToken(EmailConfirmationToken confirmationToken){
        confirmationServiceRepository.save(confirmationToken);
    }
}
