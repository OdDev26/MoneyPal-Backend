package com.user__.controller;



import com.user__.entity.Service;
import com.user__.form_error_handler.MapValidationErrorService;
import com.user__.form_error_handler.RegistrationRequestValidator;
import com.user__.request.CreateService;
import com.user__.request.RegistrationRequest;
import com.user__.service.AdminService;
import com.user__.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;
    @Autowired
    private MapValidationErrorService errorService;
    @Autowired
    private RegistrationRequestValidator validator;

    @PostMapping("auth/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult result){
        validator.validate(registrationRequest,result);
        ResponseEntity<?> errorMap = errorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;
        return adminService.registerAdmin(registrationRequest);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/view/services")
    public List<Service> viewAllServicesAvailable() {
        return userService.viewAllServicesThatCanBeOfferedAndRequested();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add/service")
    public ResponseEntity<?> addNewService(@RequestBody CreateService createService){
       return adminService.addService(createService);

    }

}
