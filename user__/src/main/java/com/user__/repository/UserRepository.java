package com.user__.repository;




import com.user__.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


    @Query(value = "select * from app_users where phone_number=?1",nativeQuery = true)
    User findByPhoneNumber(String phoneNumber);

    @Query(value = "select * from app_users where email=?1 or username=?1",nativeQuery = true)
    User findUserByEmailOrUsername(String usernameOrEmail);
}
