package br.com.ibm.cadeiabatch.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

@Configuration
@EnableDynamoDBRepositories(basePackages = "br.com.ibm.cadeiabatch.repository")
public class DynamoDBConfig {


    @Value("${amazon.aws.region}")
    private String amazonAWSRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        AmazonDynamoDB amazonDynamoDB
                = AmazonDynamoDBClientBuilder.standard()
                .withRegion(amazonAWSRegion)
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration (amazonDynamoDBEndpoint, amazonAWSRegion) )
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
        return amazonDynamoDB;
    }

}
