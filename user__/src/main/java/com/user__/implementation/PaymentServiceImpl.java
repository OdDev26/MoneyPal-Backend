package com.user__.implementation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.user__.entity.Billing;
import com.user__.entity.Payment;
import com.user__.entity.PaymentStatus;
import com.user__.entity.Wallet;
import com.user__.repository.BillingRepository;
import com.user__.repository.PaymentRepository;
import com.user__.repository.WalletRepository;
import com.user__.request.TransferRequest;
import com.user__.response.TransactionResponseDto;
import com.user__.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
import java.util.Date;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<?> payForAService(TransferRequest transferRequest) throws JsonProcessingException, ClassNotFoundException, SQLException {
        ObjectMapper objectMapper= new ObjectMapper();
       Wallet senderWallet= walletRepository.findByWalletName(transferRequest.getSenderWalletName());
       Wallet receiverWallet= walletRepository.findByWalletName(transferRequest.getReceiverWalletName());
       Double amountToTransfer= transferRequest.getAmount();
       Billing billing= billingRepository.findByInvoiceNo(transferRequest.getInvoiceNo());
       com.user__.entity.Service service = billing.getService();
       Payment payment= new Payment();

       payment.setService(service);
       payment.setPaid_by(senderWallet.getUser());
       payment.setPaid_to(receiverWallet.getUser().getId());
       payment.setBilling(billing);
       payment.setDate(new Date());
       payment.setAmountPaid(transferRequest.getAmount());
       payment.setStatus(PaymentStatus.PAID);

       paymentRepository.save(payment);

       Double senderNewBalance = senderWallet.getBalance() - amountToTransfer;
       senderWallet.setBalance(senderNewBalance);
       Double receiverNewWalletBalance= receiverWallet.getBalance()+ amountToTransfer;
       receiverWallet.setBalance(receiverNewWalletBalance);

       walletRepository.save(senderWallet);
       walletRepository.save(receiverWallet);

        TransactionResponseDto transactionResponse = getTransactionResponse(billing.getId());
        String payload= objectMapper.writeValueAsString(transactionResponse);

        rabbitTemplate.convertAndSend("Light-Weight-App",transactionResponse);


        log.info("payload {}",payload);
        log.info("transactionResponse {}",payload);
        return ResponseEntity.ok(transactionResponse);
    }

    private TransactionResponseDto getTransactionResponse(Long id) throws ClassNotFoundException, SQLException {
        String driver="com.mysql.cj.jdbc.Driver";
        String url= "jdbc:mysql://localhost:3306/light_weight_app";
        Class.forName(driver);
        Connection connection= DriverManager.getConnection(url,"root","odamic26");
        String num= String.valueOf(id);
        String query=  "select p.amount_paid as amount,(select s.service_name  from services s join payments p on p.services_id= s.id where p.billings_id=" +num+" and \n" +
                "p.paid_by= b.app_users_id ) \n" +
                "as service,(select concat(a.first_name,\" \",a.last_name) \n" +
                "from app_users a join payments p on p.paid_by= a.id where p.billings_id="+num+" and \n" +"p.paid_by= b.app_users_id ) \n" +
                "as sender,(select concat(a.first_name,\" \",a.last_name) \n" +
                "from app_users a join payments p on a.id=p.paid_to where p.billings_id=" +num+
                " and \n" +
                "p.paid_by= b.app_users_id ) as receiver,\n" +
                "(select email  from app_users a join payments p on a.id=p.paid_to where p.billings_id=" +num+
                " and \n" +
                "p.paid_by= b.app_users_id ) as emailc,\n" +
                "(select email  from app_users a join payments p on a.id=p.paid_by where p.billings_id=" +num+
                " and \n" +
                "p.paid_by= b.app_users_id) as emailp,\n" +
                "b.invoice_no as invoice,\n" +
                "p.date as time from payments p join billings b on p.billings_id= b.id where p.billings_id="+num+
                " and \n" +
                "p.paid_by= b.app_users_id;";
        Statement statement= connection.createStatement();
        ResultSet resultSet= statement.executeQuery(query);
        TransactionResponseDto transactionResponseDto= new TransactionResponseDto();

        while (resultSet.next()){
            Double amount= resultSet.getDouble("amount");
            String service= resultSet.getString("service");
            String emailP= resultSet.getString("emailc");
            String emailC =resultSet.getString("emailp");
            String sender= resultSet.getString("sender");
            String receiver= resultSet.getString("receiver");
            Date time= resultSet.getDate("time");
            String invoice= resultSet.getString("invoice");


            transactionResponseDto.setAmount(amount);
            transactionResponseDto.setService(service);
            transactionResponseDto.setEmailC(emailC);
            transactionResponseDto.setEmailP(emailP);
            transactionResponseDto.setService(service);
            transactionResponseDto.setSender(sender);
            transactionResponseDto.setReceiver(receiver);
            transactionResponseDto.setTime(time);
            transactionResponseDto.setInvoice(invoice);

        }
        statement.close();

        return transactionResponseDto;
    }
}


