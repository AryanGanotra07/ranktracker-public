package com.codingee.ranked.ranktracker.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${dfs.api.login}")
    private String login;

    @Value("${dfs.api.password}")
    private String password;


    @Bean
    @Qualifier("dfs")
    public RestTemplate dfsRestTemplate() {

        return new RestTemplateBuilder()
                .interceptors((ClientHttpRequestInterceptor) (httpRequest, bytes, execution) -> {
                    String credentials = login + ":" + password;
                    String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
                    httpRequest.getHeaders().setBasicAuth(encodedCredentials);
                    httpRequest.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    httpRequest.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    return execution.execute(httpRequest, bytes);
                })
                .build();
    }

}