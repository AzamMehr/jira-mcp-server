package com.mcp.service;

import com.mcp.config.JiraApiConfiguration;
import com.mcp.dto.TicketQueryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketQueryService {

    private static final Logger logger = LoggerFactory.getLogger(TicketQueryService.class);

    private final RestClient restClient;
    private final JiraApiConfiguration jiraApiConfiguration;

    public TicketQueryService(RestClient restClient, JiraApiConfiguration jiraApiConfiguration) {
        this.restClient = restClient;
        this.jiraApiConfiguration = jiraApiConfiguration;
    }


    @Tool(description = """
         Search Jira issues using JQL (Jira Query Language).
         Parameters:
         1. jql - The JQL search query (e.g., 'project = DEMO AND status = Open')
         2. maxResults - Optional maximum number of results to return
         """)
    public TicketQueryDTO.SearchResponse searchIssues(String jql, Integer maxResults) {
        String endpoint = "/search";
        Map<String, Object> params = new HashMap<>();
        params.put("jql", jql);
        System.out.println("jql : " + jql);
        System.out.println("maxResults : " + maxResults);
        if (maxResults != null) {
            params.put("maxResults", maxResults);
        }

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .queryParam("jql", jql)
                .queryParam("maxResults", 10)
                .build()
                .encode()
                .toUri();
        ResponseEntity<TicketQueryDTO.SearchResponse> response = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(TicketQueryDTO.SearchResponse.class);
        logger.info("Response : {} ",response);
        return response.getBody() != null ? response.getBody() : new TicketQueryDTO.SearchResponse(List.of());
    }

    @Tool(description = """
         Retrieve a specific Jira issue by its key.
         Parameter:
         1. issueKey - The Jira issue key to retrieve (e.g., 'DEMO-123')
         """)
    public TicketQueryDTO.GetIssueResponse getIssue(String issueKey) {
        String endpoint = "/issue/" + issueKey;

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {

            ResponseEntity<TicketQueryDTO.GetIssueResponse> response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .toEntity(TicketQueryDTO.GetIssueResponse.class);
            logger.info("Response about ticket: {} ",response);

            return response.getBody() != null ? response.getBody() : new TicketQueryDTO.GetIssueResponse("", "", null);
        } catch (Exception e) {
            logger.error("Error retrieving JIRA issue: {}", e.getMessage());
            return new TicketQueryDTO.GetIssueResponse("", "", null);
        }
    }
}
