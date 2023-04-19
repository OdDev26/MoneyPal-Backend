package com.user__.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@Data
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String first_name;
    private String last_name;
    private String email;
    @Column(unique = true)
    private String username;
    private String password;
    @Transient
    private String confirmPassword;
    @Column(unique = true)
    private String phoneNumber;
    private LocalDate date_of_birth;
    private Boolean enabled;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private Set<Billing> billings= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "paid_by")
    private Set<Payment> payments= new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
    private Wallet wallet;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "app_users_roles",
    joinColumns =@JoinColumn(name = "app_users_id",
            referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "app_users_service_actions",
    joinColumns = @JoinColumn(name = "app_users_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "service_actions_id"))
    private Set<ServiceAction> serviceActions;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "app_users_services",
            joinColumns = @JoinColumn(name = "app_users_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private Set<UserServiceServiceAction> userServiceServiceActions;


    public User(String first_name, String last_name, String email, String userName, String password, String confirmPassword,LocalDate date_of_birth,String phoneNumber) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.date_of_birth=date_of_birth;
        this.phoneNumber= phoneNumber;

    }
}
