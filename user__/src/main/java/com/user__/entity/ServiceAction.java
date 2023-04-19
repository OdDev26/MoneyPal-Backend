package com.user__.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "service_actions")
@Data
@NoArgsConstructor
public class ServiceAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "serviceAction")
    private Set<UserServiceServiceAction> userServiceServiceActions;

}
