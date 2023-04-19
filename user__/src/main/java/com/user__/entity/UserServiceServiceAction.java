package com.user__.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "app_users_service_sactions")
@Data
public class UserServiceServiceAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "app_users_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name ="services_id")
    private Service service;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "service_action_id")
    private ServiceAction serviceAction;

}
