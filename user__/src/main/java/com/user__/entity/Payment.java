package com.user__.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long paid_to;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paid_by")
    private User paid_by;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Double amountPaid;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "services_id")
    private Service service;
    private Date date;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "billings_id")
    private Billing billing;



}
