package com.user__.controller;


import com.user__.request.RequestService;
import com.user__.response.BillingResponseDto;
import com.user__.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class BillingController {
    @Autowired
    private BillingService billingService;

    @PostMapping("/request/service")
//    @PreAuthorize("hasRole('USER')")
    public BillingResponseDto requestService(@RequestBody RequestService requestService) throws SQLException, ClassNotFoundException {
        return billingService.requestService(requestService);
    }

}
