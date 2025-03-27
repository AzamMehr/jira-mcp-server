package com.mcp.service;

import com.mcp.config.JiraApiConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import java.util.List;

public abstract class BaseJiraService {

    protected final JiraApiConfiguration jiraApiConfiguration;
    protected final RestClient restClient;
    protected final HttpHeaders headers;

    public BaseJiraService(JiraApiConfiguration jiraApiConfiguration) {
        this.jiraApiConfiguration = jiraApiConfiguration;
        this.restClient = RestClient.create(jiraApiConfiguration.apiUrl());
        this.headers = createHeaders();
    }

    protected RestClient getRestClient() {
        return restClient;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(this.jiraApiConfiguration.username(), this.jiraApiConfiguration.apiToken());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(org.springframework.http.MediaType.APPLICATION_JSON));
        return headers;
    }
}
