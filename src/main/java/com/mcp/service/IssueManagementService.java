package com.mcp.service;

import com.mcp.config.JiraApiConfiguration;
import com.mcp.dto.IssueManagementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class IssueManagementService extends BaseJiraService{

    private static final Logger logger = LoggerFactory.getLogger(IssueManagementService.class);

    public IssueManagementService(JiraApiConfiguration jiraApiConfiguration) {
        super(jiraApiConfiguration);
    }

    @Tool(description = "Create a new Jira issue")
    public IssueManagementDTO.CreateIssueResponse createIssue(IssueManagementDTO.CreateIssueRequest issueData) {
        String endpoint = "/issue";

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send POST request
            ResponseEntity<IssueManagementDTO.CreateIssueResponse> response = restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(issueData)
                    .retrieve()
                    .toEntity(IssueManagementDTO.CreateIssueResponse.class);

            // Return created issue data
            return response.getBody() != null ? response.getBody() : new IssueManagementDTO.CreateIssueResponse("", "");
        } catch (Exception e) {
            logger.error("Error creating JIRA issue: {}", e.getMessage());
            return new IssueManagementDTO.CreateIssueResponse("", "");
        }
    }

    @Tool(description = "Update an existing Jira issue")
    public ResponseEntity<Void> updateIssue(String issueKey, IssueManagementDTO.CreateIssueRequest issueData) {
        String endpoint = "/issue/" + issueKey;

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send PUT request
            ResponseEntity<Void> response = restClient.put()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(issueData)
                    .retrieve()
                    .toEntity(Void.class);

            // Return updated issue data
            return response.getStatusCode() == HttpStatus.NO_CONTENT ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error updating JIRA issue: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Tool(description = "Add a comment to an existing Jira issue")
    public IssueManagementDTO.AddCommentResponse addComment(String issueKey, String comment) {
        String endpoint = "/issue/" + issueKey + "/comment";
        Map<String, Object> data = new HashMap<>();
        data.put("body", comment);

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send POST request
            ResponseEntity<IssueManagementDTO.AddCommentResponse> response = restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(data)
                    .retrieve()
                    .toEntity(IssueManagementDTO.AddCommentResponse.class);

            // Return added comment data
            return response.getBody() != null ? response.getBody() : new IssueManagementDTO.AddCommentResponse("", "");
        } catch (Exception e) {
            logger.error("Error adding comment to JIRA issue: {}", e.getMessage());
            return new IssueManagementDTO.AddCommentResponse("", "");
        }
    }

}
