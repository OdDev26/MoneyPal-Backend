package com.notification__.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SesConfig {
    @Value("${aws.ses.secretKey}")
    private String secretKey;
    @Value("${aws.ses.accessKey}")
    private String accessKey;
    @Value("${aws.ses.region}")
    private String region;

    @Bean
    public AmazonSimpleEmailService getAmazonSESClient(){
        final BasicAWSCredentials basicAWSCredentials= new BasicAWSCredentials(accessKey,secretKey);

        AmazonSimpleEmailService client= AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region)
                .build();

        return client;
    }
}
