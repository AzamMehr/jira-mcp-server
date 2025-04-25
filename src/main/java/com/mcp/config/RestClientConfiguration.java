package com.mcp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class RestClientConfiguration {


    private final JiraApiConfiguration jiraApiConfiguration;

    public RestClientConfiguration(JiraApiConfiguration jiraApiConfiguration) {
        this.jiraApiConfiguration = jiraApiConfiguration;
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(jiraApiConfiguration.apiUrl())
                .defaultHeaders(headers -> headers.addAll(createHeaders()))
                .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                jiraApiConfiguration.username(),
                jiraApiConfiguration.apiToken()
        );
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
