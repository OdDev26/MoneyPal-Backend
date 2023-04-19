package com.user__.response;

import lombok.Data;

import java.util.List;

@Data
public class BillingResponseDto {
    private String invoice;
    private String service;
    private Object provider;
}
