package com.user__.service;


import com.user__.request.RequestService;
import com.user__.response.BillingResponseDto;

import java.sql.SQLException;

public interface BillingService {
  BillingResponseDto requestService(RequestService requestService) throws SQLException, ClassNotFoundException;
}
