package com.user__.repository;




import com.user__.entity.ServiceAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceActionRepository extends JpaRepository<ServiceAction,Long> {
    @Query(value = "SELECT * FROM service_actions sa WHERE sa.name= ?1",nativeQuery = true)
    ServiceAction offeringService(String offering);

    @Query(value = "SELECT * FROM service_actions sa WHERE sa.name= ?1",nativeQuery = true)
    ServiceAction requestingService(String requesting);


}


