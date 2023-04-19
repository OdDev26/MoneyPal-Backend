package com.user__.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name ="services" )
@Data
@JsonIgnoreProperties({"billings", "payments","serviceActions","userServiceServiceActions"})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "service")
    private Set<Billing> billings;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "service")
    private Set<Payment> payments;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "services_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_actions_id")
    )
    private Set<ServiceAction> serviceActions;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "service")
    private Set<UserServiceServiceAction> userServiceServiceActions;

}
