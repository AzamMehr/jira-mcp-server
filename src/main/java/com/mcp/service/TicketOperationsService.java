package com.mcp.service;

import com.mcp.dto.TicketOperationsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class TicketOperationsService {

    private static final Logger logger = LoggerFactory.getLogger(TicketOperationsService.class);

    private final RestClient restClient;

    public TicketOperationsService(RestClient restClient) {
        this.restClient = restClient;
    }



    @Tool(name = "create_issues",
         description = """
         Create a new Jira issue. If ticket is created successfully, the issue key will be returned.
         if it failed then null response will be returned. 
         You need to provide the issue details in the following format:
         The issueData should contain 'fields' with: project key, summary, description, and issuetype.
         Example:
         {
             "fields": {
                 "project": { "key": "DEMO" },
                 "summary": "Issue Title",
                 "description": {
                     "type": "doc",
                     "version": 1,
                     "content": [{
                         "type": "paragraph",
                         "content": [{
                             "type": "text",
                             "text": "Issue description"
                         }]
                     }]
                 },
                 "issuetype": { "name": "Task" }
             }
         }
         """)
    public String createIssue(TicketOperationsDTO.CreateIssueRequest issueData) {
        String endpoint = "/issue";
        logger.info("Issue data received : {} ",issueData);

        try {
            ResponseEntity<TicketOperationsDTO.CreateIssueResponse> response = restClient.post()
                    .uri(endpoint)
                    .body(issueData)
                    .retrieve()
                    .toEntity(TicketOperationsDTO.CreateIssueResponse.class);
            logger.info("Response received: {}", response);

            if (response.getBody() != null) {
                logger.info("Successfully created JIRA issue with key: {}", response.getBody().key());
                return response.getBody() != null ? "Issue created with key: " + response.getBody().key() : "Failed to create issue";

            } else {
                logger.error("JIRA returned empty body.");
                return "MCP failed to create issue on JIRA"; // MCP will not attempt to send null SSE
            }
        } catch (Exception e) {
            logger.error("Error creating JIRA issue: {}", e.getMessage());
            return "MCP failed to create issue on JIRA";
        }

    }

    @Tool(name = "update_issue",
         description = """
         Update an existing Jira issue. Requires two inputs:
         1. issueKey - The Jira issue key (e.g., 'DEMO-123')
         2. issueData - The updated fields in the same format as create_issues
         """)
    public String updateIssue(String issueKey, TicketOperationsDTO.CreateIssueRequest issueData) {
        String endpoint = "/issue/" + issueKey;

        try {
            // Send PUT request
            ResponseEntity<Void> response = restClient.put()
                    .uri(endpoint)
                    .body(issueData)
                    .retrieve()
                    .toEntity(Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully updated JIRA issue with key: {}", issueKey);
                return "Issue updated successfully with key: " + issueKey;

            } else {
                logger.error("Failed to update JIRA issue with key: {}", issueKey);
                return "MCP failed to create issue on JIRA";
            }

        } catch (Exception e) {
            logger.error("Failed to update JIRA issue with key because of technical issues: {}", issueKey);
            return "MCP failed to create issue on JIRA";
        }
    }

    @Tool(name = "comment_issue",
         description = """
         Add a comment to a Jira issue. Requires two inputs:
         1. issueKey - The Jira issue key (e.g., 'DEMO-123')
         2. comment - The text content of the comment as a simple string
         """)
    public String addComment(String issueKey, String comment) {
        String endpoint = "/issue/" + issueKey + "/comment";
        Map<String, Object> data = new HashMap<>();
        data.put("body", comment);

        try {

            ResponseEntity<TicketOperationsDTO.AddCommentResponse> response = restClient.post()
                    .uri(endpoint)
                    .body(data)
                    .retrieve()
                    .toEntity(TicketOperationsDTO.AddCommentResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Successfully added comment to JIRA ticket with key: {}", issueKey);
                return "Successfully added comment to JIRA ticket with key: " + issueKey;

            } else {
                logger.error("Failed to add comment to JIRA ticket with key: {}", issueKey);
                return "MCP failed to add comment to JIRA ticket with key: " + issueKey;
            }
        } catch (Exception e) {
            logger.error("Error adding comment to JIRA issue: {}", e.getMessage());
            return "MCP failed to add comment to JIRA ticket with key: " + issueKey;
        }
    }
}
