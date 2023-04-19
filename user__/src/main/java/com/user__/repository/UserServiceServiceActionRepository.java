package com.user__.repository;


import com.user__.entity.UserServiceServiceAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserServiceServiceActionRepository extends JpaRepository<UserServiceServiceAction,Long> {
}
