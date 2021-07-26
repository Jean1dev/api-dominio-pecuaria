package com.binno.dominio.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.S3ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class S3Config {

    @Value("${aws.access_key_id}")
    private String awsId;

    @Value("${aws.secret_access_key}")
    private String awsKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.host}")
    private String host;

    @Bean
    public AmazonS3 s3client() {
        if (Objects.nonNull(host))
            return criarS3Fake();

        BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                .build();
    }

    private AmazonS3 criarS3Fake() {
        BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3Client s3Client = new AmazonS3Client(awsCred, new ClientConfiguration().withSignerOverride("S3SignerType"));

        s3Client.setS3ClientOptions(S3ClientOptions.builder()
                .setPathStyleAccess(true)
                .build());

        s3Client.setEndpoint(host);
        return s3Client;
    }
}
