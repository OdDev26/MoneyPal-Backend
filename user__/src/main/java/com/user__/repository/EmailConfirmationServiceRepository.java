package com.user__.repository;

import com.user__.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationServiceRepository extends JpaRepository<EmailConfirmationToken,Long> {
    Optional<EmailConfirmationToken> findByToken(String token);

    @Query(value = "select * from email_confirmation_token where token=?1",nativeQuery = true)
    EmailConfirmationToken findUsingToken(String token);
}
