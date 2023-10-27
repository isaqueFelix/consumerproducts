package br.com.ibm.cadeiabatch.sns;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnsConfig {

    @Value("${amazon.aws.region}")
    private String awsRegion;

    @Value("${aws.sns.topic.product.events.arn}")
    private String productEventsTopic;

    @Value("${amazon.aws.accesskey}")
    private String accesskey;

    @Value("${amazon.aws.secretkey}")
    private String secretkey;

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accesskey, secretkey)))
                .build();
    }

    @Bean(name = "productEventsTopic")
    public Topic snsProductEventsTopic() {
        return new Topic().withTopicArn(productEventsTopic);
    }
}
