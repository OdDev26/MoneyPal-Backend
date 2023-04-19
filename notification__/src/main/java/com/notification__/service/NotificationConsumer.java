package com.notification__.service;



import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.notification__.config.SesConfig;
import com.user__.response.FundingResponse;
import com.user__.response.TransactionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class NotificationConsumer {

    @Autowired
    private AmazonSimpleEmailService amazonSimpleEmailService;


    private static  final String FUNDING_TEMPLATE= "Funding-Alert1";
    private static  final String TRANSACTION_TEMPLATE= "Transaction-Alert";

    @RabbitListener(queues = "${rabbitmq.queue.transfer.response}")
    public void consumeAndSendTransferNotification(TransactionResponseDto transactionResponse) {
        log.info("message consumed {}",transactionResponse);
        sendTransactionNotification(transactionResponse.getEmailP(),transactionResponse.getEmailC(),transactionResponse);
        log.info("email sent");
    }



    @RabbitListener(queues = "${rabbitmq.queue.funding.response}")
    public void consumeAndSendFundingNotification(FundingResponse fundingResponse){
        sendFundingNotification1(fundingResponse.getWalletName(),fundingResponse.getBalance(), fundingResponse.getFunderEmail(),fundingResponse.getFundingAmount());
        log.info("message consumed {}",fundingResponse);
        log.info("Funding amount "+ fundingResponse.getFundingAmount());
        log.info("email sent");
    }

    public void sendFundingNotification1(String walletName, Double balance, String funderEmail, Double amount) {
        createFundingAlertTemplate(walletName, balance,amount);
       try {
           sendFundingNotif(funderEmail);
       }finally {
           deleteFundingTemplate();
       }
    }
    private void createFundingAlertTemplate(String walletName, Double balance, Double amount) {
        Template template= new Template();

        template.setTemplateName(FUNDING_TEMPLATE);
        template.setSubjectPart("Funding Alert");

        StringBuilder text= new StringBuilder();
        text.append("<!DOCTYPE html>\n" +
                "<body>\n" +
                "  <table style=\"border-collapse: collapse; margin: 25px 0; font-size: 0.9em; min-width:300px;   box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);\">\n" +
                "    <thead style=\"background-color: #000000; color: #ffffff; text-align: left;\">\n" +
                "        <tr style=\"padding: 12px 15px;\">\n" +
                "            <th>Wallet name</th>\n" +
                "            <th>Balance</th>\n" +
                "            <th>amount</th>\n" +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "        <tr>\n");


        text.append("<td>"+walletName+"</td>\n");
        text.append("<td>"+balance+"</td>\n");
        text.append("<td>"+amount+"</td>\n");
        template.setHtmlPart(text.toString());
        CreateTemplateRequest createTemplateRequest= new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult templateResult= amazonSimpleEmailService.createTemplate(createTemplateRequest);
        System.out.println("Template created successfully"+templateResult);
    }


    public void sendFundingNotif(String funderEmail) {

        List<BulkEmailDestination> listBulkEmailDestination = new ArrayList<BulkEmailDestination>();
        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = null;
        List<String> recipients= new ArrayList<>();

            recipients.add(0, funderEmail);

        try {

            for(String email : recipients) {

                BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
                bulkEmailDestination.setDestination(new Destination(Arrays.asList(email)));
                listBulkEmailDestination.add(bulkEmailDestination);
            }

            sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
            sendBulkTemplatedEmailRequest.setSource("odmichael26@gmail.com");
            sendBulkTemplatedEmailRequest.setTemplate(FUNDING_TEMPLATE);
            sendBulkTemplatedEmailRequest.setDefaultTemplateData("{\"FULL_NAME\":\"mmmmm\", \"EMAIL\":\"tech.lipsa20@gmail.com\"}");
            sendBulkTemplatedEmailRequest.setDestinations(listBulkEmailDestination);
            SendBulkTemplatedEmailResult res = amazonSimpleEmailService.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);

            System.out.println("response from aws ======================================" +res);
            System.out.println("======================================");

            for(BulkEmailDestinationStatus status : res.getStatus()) {
                System.out.println("status==========" + status.getStatus());
                System.out.println("message Id==========" + status.getMessageId());
            }



        }catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
            ex.printStackTrace();
        }


    }

    public void deleteFundingTemplate(){
        DeleteTemplateRequest deleteTemplateRequest= new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(FUNDING_TEMPLATE);
        DeleteTemplateResult result= amazonSimpleEmailService.deleteTemplate(deleteTemplateRequest);
        System.out.println("response after template delete"+result);
        System.out.println("template_name"+ " deleted");
    }


    private void sendTransactionNotification(String emailP, String emailC, TransactionResponseDto transactionResponse) {
        createTransactionAlertTemplate(transactionResponse);
        try {
            sendTransactionNotif(emailP,emailC);
        }finally {
            deleteTransactionTemplate();
        }
    }

    private void createTransactionAlertTemplate(TransactionResponseDto transactionResponse) {

        Template template= new Template();

        template.setTemplateName(TRANSACTION_TEMPLATE);
        template.setSubjectPart("Transaction Alert");


        String sender = transactionResponse.getSender();
        String receiver = transactionResponse.getReceiver();
        String invoice = transactionResponse.getInvoice();
        String amount = transactionResponse.getAmount().toString();
        String service = transactionResponse.getService();
        String time = String.valueOf(transactionResponse.getTime());

        StringBuilder text= new StringBuilder();
        text.append("<!DOCTYPE html>\n" +
                "<body>\n" +
                "  <table style=\"border-collapse: collapse; margin: 25px 0; font-size: 0.9em; min-width:650px;   box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);\">\n" +
                "    <thead style=\"background-color: #000000; color: #ffffff; text-align: left;\">\n" +
                "        <tr style=\"padding: 12px 15px;\">\n" +
                "            <th>Sender</th>\n" +
                "            <th>Receiver</th>\n" +
                "          <th>Amount</th>\n" +
                "          <th>Date</th>\n" +
                "          <th>Invoice</th>\n" +
                "          <th>Service</th>\n" +
                "        </tr>\n" +
                "    </thead>\n" +
                "    <tbody>\n" +
                "        <tr>\n");
        text.append("<td>"+sender+"</td>\n");
        text.append("<td>"+receiver+"</td>\n");
        text.append("<td>"+amount+"</td>\n");
        text.append("<td>"+time+"</td>\n");
        text.append("<td>"+invoice+"</td>\n");
        text.append("<td>"+service+"</td>\n");
        text.append(
                "</tr>\n" +
                        "</tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>");

        template.setHtmlPart(text.toString());
        CreateTemplateRequest createTemplateRequest= new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);

        CreateTemplateResult templateResult= amazonSimpleEmailService.createTemplate(createTemplateRequest);
        System.out.println("Template created successfully"+templateResult);

    }

    private void sendTransactionNotif(String providerEmail, String consumerEmail ){
        List<BulkEmailDestination> listBulkEmailDestination = new ArrayList<BulkEmailDestination>();
        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = null;
        List<String> recipients= new ArrayList<>();

        recipients.add(0, providerEmail);
        recipients.add(1,consumerEmail);

        try {

            for(String email : recipients) {

                BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
                bulkEmailDestination.setDestination(new Destination(Arrays.asList(email)));
                listBulkEmailDestination.add(bulkEmailDestination);
            }

            sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
            sendBulkTemplatedEmailRequest.setSource("odmichael26@gmail.com");
            sendBulkTemplatedEmailRequest.setTemplate(TRANSACTION_TEMPLATE);
            sendBulkTemplatedEmailRequest.setDefaultTemplateData("{\"FULL_NAME\":\"mmmmm\", \"EMAIL\":\"tech.lipsa20@gmail.com\"}");
            sendBulkTemplatedEmailRequest.setDestinations(listBulkEmailDestination);
            SendBulkTemplatedEmailResult res = amazonSimpleEmailService.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);

            System.out.println("response from aws ======================================" +res);
            System.out.println("======================================");

            for(BulkEmailDestinationStatus status : res.getStatus()) {
                System.out.println("status==========" + status.getStatus());
                System.out.println("message Id==========" + status.getMessageId());
            }



        }catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public void deleteTransactionTemplate(){
        DeleteTemplateRequest deleteTemplateRequest= new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(TRANSACTION_TEMPLATE);
        DeleteTemplateResult result= amazonSimpleEmailService.deleteTemplate(deleteTemplateRequest);
        System.out.println("response after template delete"+result);
        System.out.println("template_name"+ " deleted");
    }

}
