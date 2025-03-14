package com.mcp.service;

import com.mcp.config.JiraApiConfiguration;
import com.mcp.dto.IssueSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IssueSearchService extends BaseJiraService {

    private static final Logger logger = LoggerFactory.getLogger(IssueSearchService.class);

    public IssueSearchService(JiraApiConfiguration jiraApiConfiguration) {
        super(jiraApiConfiguration);
    }


    @Tool(description = "Search Jira issues using JQL")
    public IssueSearchDTO.SearchResponse searchIssues(String jql, Integer maxResults) {
        String endpoint = "/search";
        Map<String, Object> params = new HashMap<>();
        params.put("jql", jql);

        if (maxResults != null) {
            params.put("maxResults", maxResults);
        }

        // Build the URL with query parameters
        URI uri = UriComponentsBuilder.fromHttpUrl(this.jiraApiConfiguration.apiUrl() + endpoint)
                .queryParam("jql", jql)
                .queryParam("maxResults", 10)
                .build()
                .encode()
                .toUri();

        // Send GET request
        ResponseEntity<IssueSearchDTO.SearchResponse> response = restClient.get()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .toEntity(IssueSearchDTO.SearchResponse.class);

        // Return issue list
        return response.getBody() != null ? response.getBody() : new IssueSearchDTO.SearchResponse(List.of());
    }

    @Tool(description = "Retrieve a specific Jira issue by its key")
    public IssueSearchDTO.GetIssueResponse getIssue(String issueKey) {
        String endpoint = "/issue/" + issueKey;

        URI uri = UriComponentsBuilder.fromHttpUrl(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send GET request
            ResponseEntity<IssueSearchDTO.GetIssueResponse> response = restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .toEntity(IssueSearchDTO.GetIssueResponse.class);

            // Return issue data
            return response.getBody() != null ? response.getBody() : new IssueSearchDTO.GetIssueResponse("", "", null);
        } catch (Exception e) {
            logger.error("Error retrieving JIRA issue: {}", e.getMessage());
            return new IssueSearchDTO.GetIssueResponse("", "", null);
        }
    }

}
