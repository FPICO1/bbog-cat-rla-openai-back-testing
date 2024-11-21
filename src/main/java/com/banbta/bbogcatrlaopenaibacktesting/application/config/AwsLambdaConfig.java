package com.banbta.bbogcatrlaopenaibacktesting.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import java.time.Duration;


@Configuration
public class AwsLambdaConfig {

    @Bean
    public LambdaAsyncClient lambdaAsyncClient() {
        return LambdaAsyncClient.builder()
                .httpClient(NettyNioAsyncHttpClient.builder()
                        .connectionTimeout(Duration.ofSeconds(60))
                        .readTimeout(Duration.ofSeconds(120))
                        .maxConcurrency(50)
                        .build())
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .retryPolicy(RetryPolicy.defaultRetryPolicy())
                        .apiCallTimeout(Duration.ofSeconds(120))
                        .build())
                .build();
    }
}