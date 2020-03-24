package com.gabia.project.internproject.remoteService.configs;

import com.gabia.project.internproject.remoteService.handler.RestTemplateErrorHandler;
import com.gabia.project.internproject.remoteService.interceptor.RestTemplateCustomLoggingInterceptor;
import lombok.NoArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@NoArgsConstructor
public class VendorsRestTemplateConfig {

    private RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

    @Value("${spring.restTemplate.connect.timeout}")
    private int CONNECT_TIMEOUT;

    @Value("${spring.restTemplate.read.timeout}")
    private int READ_TIMEOUT;

    @Value("${spring.restTemplate.httpClient.maxConnTotal}")
    private int MAX_CONN_TOTAL;

    @Value("${spring.restTemplate.httpClient.maxConnPerRoute}")
    private int MAX_CONN_PER_ROUTE;

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        factory.setReadTimeout(READ_TIMEOUT);
        factory.setConnectTimeout(CONNECT_TIMEOUT);

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();

        factory.setHttpClient(httpClient);
        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate dataDreamRestTemplate() {
        return restTemplateBuilder.setConnectTimeout(Duration.ofMinutes(CONNECT_TIMEOUT)).build();
    }

    @Bean
    public RestTemplate googlePlaceRestTemplate() {
        return restTemplateBuilder.additionalInterceptors(new RestTemplateCustomLoggingInterceptor())
                .errorHandler(new RestTemplateErrorHandler())
                .setConnectTimeout(Duration.ofMinutes(CONNECT_TIMEOUT)).build();
    }


}