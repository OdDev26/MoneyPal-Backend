package com.user__.implementation;


import com.user__.entity.Billing;
import com.user__.entity.ServiceAction;
import com.user__.entity.User;
import com.user__.entity.UserServiceServiceAction;
import com.user__.repository.*;
import com.user__.request.RequestService;
import com.user__.response.BillingResponseDto;
import com.user__.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

import static com.user__.database_auth_params.DatabaseAuthParams.*;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceActionRepository serviceActionRepository;

    @Autowired
    private UserServiceServiceActionRepository userServiceServiceActionRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Override
    public BillingResponseDto requestService(RequestService requestService) throws SQLException, ClassNotFoundException {

        User user = userRepository.findByEmail(requestService.getEmail()).get();
        com.user__.entity.Service service = serviceRepository.findByServiceName(requestService.getServiceName());


        ServiceAction serviceAction = serviceActionRepository.requestingService("Requesting");


        UserServiceServiceAction userServiceServiceAction = new UserServiceServiceAction();

        userServiceServiceAction.setServiceAction(serviceAction);
        userServiceServiceAction.setService(service);
        userServiceServiceAction.setUser(user);

        userServiceServiceActionRepository.save(userServiceServiceAction);

        return generateBill(requestService);
    }

    private BillingResponseDto generateBill(RequestService requestService) throws ClassNotFoundException, SQLException {
        Billing billing = new Billing();
        billing.setDate(new Date());

        User user = userRepository.findByEmail(requestService.getEmail()).get();
        com.user__.entity.Service service = serviceRepository.findByServiceName(requestService.getServiceName());

        String invoiceNo = UUID.randomUUID().toString();

        billing.setService(service);
        billing.setUser(user);
        billing.setInvoiceNo(invoiceNo);


        billingRepository.save(billing);
       BillingResponseDto billingResponseDto= generateBillResponse(service.getId());
        return billingResponseDto;
    }
    private BillingResponseDto generateBillResponse(Long id) throws ClassNotFoundException, SQLException {
        String driver="com.mysql.cj.jdbc.Driver";
        Class.forName(driver);

        String num= String.valueOf(id);

        Connection connection= DriverManager.getConnection(URL,USER_NAME,PASSWORD);
        Statement statement= connection.createStatement();
        String query="select b.invoice_no as invoice, \n" +
                "  (select s.service_name from services s where id=" +num+
                ") as service, \n" +
                "(select json_arrayagg(a.phone_number) from app_users a where a.id in \n" +
                "(select ausa.app_users_id from app_users_service_sactions ausa where ausa.service_action_id=1\n" +
                "and ausa.services_id=" +num+
                "))  as provider from billings b  \n" +
                "where b.services_id=" +num+
                " and b.date=now();";
        ResultSet resultSet= statement.executeQuery(query);
        BillingResponseDto billingResponseDto= new BillingResponseDto();

        while (resultSet.next()){
            String invoice= resultSet.getString("invoice");
            String service= resultSet.getString("service");
            Object provider= resultSet.getObject("provider");


            billingResponseDto.setService(service);
            billingResponseDto.setInvoice(invoice);
            billingResponseDto.setProvider( provider);
        }
        statement.close();
        return billingResponseDto;

    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driver="com.mysql.cj.jdbc.Driver";
        Class.forName(driver);

        Connection connection= DriverManager.getConnection(URL,USER_NAME,PASSWORD);
        Statement statement= connection.createStatement();
        String query="select b.invoice_no as invoice, " +
                "(select s.service_name from services s where id=2) as service," +
                "(select json_arrayagg(a.phone_number) from app_users a where a.id in" +
                "(select ausa.app_users_id from app_users_service_sactions ausa where ausa.service_action_id=2 " +
                "and ausa.services_id=2))  as provider from billings b " +
                "where b.services_id=2 and b.date='2022-06-13 14:02:26';";
        ResultSet resultSet= statement.executeQuery(query);
        BillingResponseDto billingResponseDto= new BillingResponseDto();

        while (resultSet.next()){
            String invoice= resultSet.getString("invoice");
            String service= resultSet.getString("service");
            Object provider= resultSet.getObject("provider");


            billingResponseDto.setService(service);
            billingResponseDto.setInvoice(invoice);
            billingResponseDto.setProvider( provider);
        }
        statement.close();

        System.out.println(billingResponseDto);
    }
}

