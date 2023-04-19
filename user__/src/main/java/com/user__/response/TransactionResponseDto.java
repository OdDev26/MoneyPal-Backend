package com.user__.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class TransactionResponseDto implements Serializable {
  private Double amount;
  private String invoice;
  private String sender;
  private String receiver;
  private String emailC;
  private String emailP;
  private Date time;
  private String service;
}
