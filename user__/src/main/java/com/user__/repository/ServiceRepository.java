package com.user__.repository;



import com.user__.entity.Service;
import com.user__.response.ServicesOffered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service,Long> {
    Service findByServiceName(String serviceName);

    @Query(value = "SELECT s.id, s.service_name FROM services s",nativeQuery = true)
    List<Service> fetchAllServices();

    @Query(value = "SELECT CONCAT(a.first_name,\" \",a.last_name)AS name,(" +
            "SELECT s.service_name FROM services s WHERE s.id= sac.services_id) AS" +
            " service FROM app_users a " +
            "INNER JOIN service_actions sac ON a.id= sac.app_users_id " +
            "WHERE sac.service_action_type_id=1;",nativeQuery = true)
    List<ServicesOffered> retrieveServicesOffered();
}
