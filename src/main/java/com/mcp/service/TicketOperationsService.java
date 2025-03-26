package com.mcp.service;

import com.mcp.config.JiraApiConfiguration;
import com.mcp.dto.TicketOperationsDTO;
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
public class TicketOperationsService extends BaseJiraService{

    private static final Logger logger = LoggerFactory.getLogger(TicketOperationsService.class);

    public TicketOperationsService(JiraApiConfiguration jiraApiConfiguration) {
        super(jiraApiConfiguration);
    }

    @Tool(name = "create_issues",
         description = """
         Create a new Jira issue. You need to provide the issue details in the following format:
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
        System.out.println("Issue data received : " + issueData);
        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send POST request
            ResponseEntity<TicketOperationsDTO.CreateIssueResponse> response = restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(issueData)
                    .retrieve()
                    .toEntity(TicketOperationsDTO.CreateIssueResponse.class);
            logger.info("Response received: {}", response);
            logger.info("response.getBody(): {}", response.getBody());
            // Return created issue data
            return "Issue created with key: "+response.getBody();
        } catch (Exception e) {
            logger.error("Error creating JIRA issue: {}", e.getMessage());
            return "It failed to create issue";
        }
    }

    @Tool(name = "update_issue",
         description = """
         Update an existing Jira issue. Requires two inputs:
         1. issueKey - The Jira issue key (e.g., 'DEMO-123')
         2. issueData - The updated fields in the same format as create_issues
         """)
    public ResponseEntity<Void> updateIssue(String issueKey, TicketOperationsDTO.CreateIssueRequest issueData) {
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

    @Tool(name = "comment_issue",
         description = """
         Add a comment to a Jira issue. Requires two inputs:
         1. issueKey - The Jira issue key (e.g., 'DEMO-123')
         2. comment - The text content of the comment as a simple string
         """)
    public TicketOperationsDTO.AddCommentResponse addComment(String issueKey, String comment) {
        String endpoint = "/issue/" + issueKey + "/comment";
        Map<String, Object> data = new HashMap<>();
        data.put("body", comment);

        URI uri = UriComponentsBuilder.fromUriString(this.jiraApiConfiguration.apiUrl() + endpoint)
                .build()
                .encode()
                .toUri();

        try {
            // Send POST request
            ResponseEntity<TicketOperationsDTO.AddCommentResponse> response = restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(data)
                    .retrieve()
                    .toEntity(TicketOperationsDTO.AddCommentResponse.class);

            // Return added comment data
            return response.getBody() != null ? response.getBody() : new TicketOperationsDTO.AddCommentResponse("", "");
        } catch (Exception e) {
            logger.error("Error adding comment to JIRA issue: {}", e.getMessage());
            return new TicketOperationsDTO.AddCommentResponse("", "");
        }
    }

}
