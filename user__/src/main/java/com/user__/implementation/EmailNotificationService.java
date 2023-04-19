package com.user__.implementation;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EmailNotificationService {
    @Autowired
    private AmazonSimpleEmailService amazonSimpleEmailService;

    String EMAIL_CONFIRMATION_TEMPLATE = "Confirm-email";

    public void createEmailTemplate(String token) {

        Template template = new Template();

        template.setTemplateName(EMAIL_CONFIRMATION_TEMPLATE);
        template.setSubjectPart("Confirm your email");


        StringBuilder link= new StringBuilder();
        link.append("<html><h1> Click this link to verify your email, the link expires after 15 minutes :"
        );
        link.append("<h1>");
        link.append("<a");
        link.append(" href=http://localhost:9000/api/v1/validate/email?token=");
        link.append(token);
        link.append(">");
        link.append("Verify email");
        link.append("</a>");
        link.append("<h1>");
        link.append("</html>");
        template.setHtmlPart(link.toString());
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult templateResult = amazonSimpleEmailService.createTemplate(createTemplateRequest);
        System.out.println("Template created successfully" + templateResult);
    }

    public void sendConfirmationEmail(String customerEmail) {

        List<BulkEmailDestination> listBulkEmailDestination = new ArrayList<BulkEmailDestination>();
        SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest = null;
        List<String> recipients = new ArrayList<>();

        recipients.add(0, customerEmail);

        try {

            for (String email : recipients) {

                BulkEmailDestination bulkEmailDestination = new BulkEmailDestination();
                bulkEmailDestination.setDestination(new Destination(Arrays.asList(email)));
                listBulkEmailDestination.add(bulkEmailDestination);
            }

            sendBulkTemplatedEmailRequest = new SendBulkTemplatedEmailRequest();
            sendBulkTemplatedEmailRequest.setSource("odmichael26@gmail.com");
            sendBulkTemplatedEmailRequest.setTemplate(EMAIL_CONFIRMATION_TEMPLATE);
            sendBulkTemplatedEmailRequest.setDefaultTemplateData("{\"FULL_NAME\":\"mmmmm\", \"EMAIL\":\"tech.lipsa20@gmail.com\"}");
            sendBulkTemplatedEmailRequest.setDestinations(listBulkEmailDestination);
            SendBulkTemplatedEmailResult res = amazonSimpleEmailService.sendBulkTemplatedEmail(sendBulkTemplatedEmailRequest);

            System.out.println("response from aws ======================================" + res);
            System.out.println("======================================");

            for (BulkEmailDestinationStatus status : res.getStatus()) {
                System.out.println("status==========" + status.getStatus());
                System.out.println("message Id==========" + status.getMessageId());
            }


        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public void deleteConfirmationEmailTemplate(){
        DeleteTemplateRequest deleteTemplateRequest= new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(EMAIL_CONFIRMATION_TEMPLATE);
        DeleteTemplateResult result= amazonSimpleEmailService.deleteTemplate(deleteTemplateRequest);
        System.out.println("response after template delete"+result);
        System.out.println("template_name"+ " deleted");
    }
}



