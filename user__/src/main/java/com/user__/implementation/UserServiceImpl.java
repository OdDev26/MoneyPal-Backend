package com.user__.implementation;



import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.user__.entity.*;
import com.user__.exception.*;
import com.user__.repository.*;
import com.user__.request.OfferService;
import com.user__.request.RegistrationRequest;
import com.user__.security.CustomUserDetailsService;
import com.user__.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String EMAIL_CONFIRMATION_TEMPLATE = "Confirm-email";

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceActionRepository serviceActionRepository;
    @Autowired
    private BillingRepository billingRepository;


    @Autowired
    private UserServiceServiceActionRepository userServiceServiceActionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EmailConfirmationServiceImpl emailConfirmationService;

    @Autowired
    private EmailConfirmationServiceRepository emailConfirmationServiceRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailNotificationService emailNotificationService;


    @Override
    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {
        GenericMessage genericMessage = new GenericMessage();
        boolean userExistsByEmail = userRepository.existsByEmail(registrationRequest.getEmail());
        boolean userExistsByUsername = userRepository.existsByUsername(registrationRequest.getUserName());
        if (userExistsByEmail) {
            throw new EmailException("User with email already exists");
        } else if (userExistsByUsername) {
            throw new UsernameException("User name already exists");
        } else if (!userExistsByEmail && !userExistsByUsername) {
            User user = new User(
                    registrationRequest.getFirstName(), registrationRequest.getLastName(),
                    registrationRequest.getEmail(), registrationRequest.getUserName(),
                    passwordEncoder.encode(registrationRequest.getPassword()), registrationRequest.getConfirmPassword(),registrationRequest.getDateOfBirth(),
                    registrationRequest.getPhoneNumber()
            );
            user.setEnabled(false);
            genericMessage.setMessage("User successfully registered");
            Role role = roleRepository.findByName("ROLE_USER").get();
            user.setRoles(Collections.singleton(role));
            userRepository.saveAndFlush(user);

            //Todo: Send confirmation email to update enabled to true
            generateAndSendToken(user.getId());
        }
        return new ResponseEntity<>(genericMessage, HttpStatus.CREATED);
    }

    private void generateAndSendToken(Long id) {
        User user = userRepository.findById(id).get();
        UUID token = UUID.randomUUID();
        String confirmationToken = token.toString();
        EmailConfirmationToken confirmationToken1 =
                new EmailConfirmationToken(
                        confirmationToken, LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user

                );

        emailConfirmationService.saveConfirmationToken(confirmationToken1);


        emailNotificationService.createEmailTemplate(confirmationToken);
        emailNotificationService.sendConfirmationEmail(user.getEmail());
        emailNotificationService.deleteConfirmationEmailTemplate();

    }

    @Override
    public ResponseEntity<?> validateToken(String token) {
        EmailConfirmationToken confirmationToken = emailConfirmationServiceRepository.findUsingToken(token);
        System.out.println(confirmationToken);
        User user = confirmationToken.getUser();

        System.out.println("User " + user);

        GenericMessage genericMessage = new GenericMessage();
        genericMessage.setMessage("Token has expired");
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(genericMessage, HttpStatus.NOT_ACCEPTABLE);
        } else {
            user.setEnabled(true);
            emailConfirmationServiceRepository.deleteById(confirmationToken.getId());
        }
        return null;
    }

    @Override
    public boolean checkIfUserEmailHasBeenValidated(String username) {
        try {
            User user = userRepository.findUserByEmailOrUsername(username);
            Boolean userIsEnabled = user.getEnabled();
            Set<Role> roles = user.getRoles();

            StringBuilder roles1 = new StringBuilder();

            for (Role role :
                    roles) {
                roles1.append(role.getName() + " ");
            }
            if (roles1.toString().contains("ROLE_ADMIN")) {
                return true;
            } else if (userIsEnabled) {
                return true;
            }


        } catch (Exception e) {
            throw new InvalidLoginException("Unverified credential");
        }
        return false;
}

    @Override
    public String offerService(OfferService offerService) {
        String email = offerService.getEmail();
        User userByEmail = userRepository.findByEmail(email).get();
        com.user__.entity.Service service = serviceRepository.findByServiceName(offerService.getServiceName());
        ServiceAction serviceAction = serviceActionRepository.offeringService("Offering");

        UserServiceServiceAction userServiceServiceAction = new UserServiceServiceAction();

        userServiceServiceAction.setServiceAction(serviceAction);
        userServiceServiceAction.setService(service);
        userServiceServiceAction.setUser(userByEmail);
        userServiceServiceActionRepository.save(userServiceServiceAction);
        

        return "Service successfully offered";

    }


    @Override
    public List<com.user__.entity.Service> viewAllServicesThatCanBeOfferedAndRequested() {
        return serviceRepository.fetchAllServices();
    }

    @Override
    public ResponseEntity<?> getWalletDetails1(String usernameOrEmail) {
//        System.out.println(email);
//        System.out.println(username);
//        log.info("email {}",email);
        log.info("usernameOrEmail {}",usernameOrEmail);

        User user= userRepository.findUserByEmailOrUsername(usernameOrEmail);
        System.out.println(user.getPassword());
        Long id= user.getId();
        Wallet wallet= walletRepository.findWalletByUserId(id);
        return ResponseEntity.ok(wallet);
    }

    @Override
    public ResponseEntity<?> getWalletDetails2(String usernameOrEmail) {
        log.info(usernameOrEmail);
        User user= userRepository.findUserByEmailOrUsername(usernameOrEmail);
        Long id= user.getId();
        Wallet wallet= walletRepository.findWalletByUserId(id);
        return ResponseEntity.ok(wallet);
    }

    @Override
    public com.user__.entity.Service viewAllServicesById(Long id) {
        com.user__.entity.Service service= serviceRepository.findById(id).get();
        return service;
    }



    @Override
    public ResponseEntity<?> getWalletDetails(String email, String username) {
        log.info("email {}",email);
        log.info("username {}",username);
        User user= userRepository.findUserByEmailOrUsername(email);
        Long id= user.getId();
        Wallet wallet= walletRepository.findWalletByUserId(id);
        return ResponseEntity.ok(wallet);
    }
}
