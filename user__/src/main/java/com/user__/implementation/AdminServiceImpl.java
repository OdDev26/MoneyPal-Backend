package com.user__.implementation;



import com.user__.entity.Role;
import com.user__.entity.User;
import com.user__.exception.AddServiceErrorMessage;
import com.user__.repository.RoleRepository;
import com.user__.repository.ServiceRepository;
import com.user__.repository.UserRepository;
import com.user__.request.CreateService;
import com.user__.request.RegistrationRequest;
import com.user__.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ServiceRepository serviceRepository;



    @Override
    public ResponseEntity<?> registerAdmin(RegistrationRequest registrationRequest) {
            String email = registrationRequest.getEmail();
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            boolean emailMatchesRegex = email.matches(regex);
            boolean userExistsByEmail = userRepository.existsByEmail(registrationRequest.getEmail());
            boolean userExistsByUsername = userRepository.existsByUsername(registrationRequest.getUserName());
            if (registrationRequest.getUserName().length() < 6) {
                return new ResponseEntity<>("User name must be at least 6 characters", HttpStatus.BAD_REQUEST);
            } else if (userExistsByEmail ) {
                return new ResponseEntity<>("User with email exists", HttpStatus.BAD_REQUEST);
            }
            else if (userExistsByUsername){
                return new ResponseEntity<>("User with username exists", HttpStatus.BAD_REQUEST);
            }

            else if (emailMatchesRegex && !userExistsByEmail && !userExistsByUsername) {
                User user = new User(
                        registrationRequest.getFirstName(), registrationRequest.getLastName(),
                        registrationRequest.getEmail(), registrationRequest.getUserName(),
                        passwordEncoder.encode(registrationRequest.getPassword()), registrationRequest.getConfirmPassword(),registrationRequest.getDateOfBirth(),
                        registrationRequest.getPhoneNumber()
                );

                Role role = roleRepository.findByName("ROLE_ADMIN").get();
                user.setRoles(Collections.singleton(role)); //To assign role to admin
                userRepository.save(user);
                return new ResponseEntity<>("Admin successfully registered", HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
 }

    @Override
    public ResponseEntity<?> addService(CreateService createService) {
        AddServiceErrorMessage errorResponse= new AddServiceErrorMessage();
        com.user__.entity.Service service = new com.user__.entity.Service();
        com.user__.entity.Service serviceByServiceName = serviceRepository.findByServiceName(createService.getServiceName());
        if(serviceByServiceName!=null){
           errorResponse.setMessage("Service already exists");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        else if (createService.getServiceName().equals("")) {
            errorResponse.setMessage("Enter a service name");
            return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
       else if (!createService.getServiceName().equals("")) {
            service.setServiceName(createService.getServiceName());
            serviceRepository.save(service);
            return new ResponseEntity<>("Service successfully added",HttpStatus.CREATED);
        }

       errorResponse.setMessage("Enter a service name");
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}
