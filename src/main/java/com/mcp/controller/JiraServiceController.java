package com.mcp.controller;

import com.mcp.dto.IssueManagementDTO;
import com.mcp.dto.IssueSearchDTO;
import com.mcp.service.IssueManagementService;
import com.mcp.service.IssueSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class JiraServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JiraServiceController.class);
    private final IssueSearchService issueSearchService;
    private final IssueManagementService issueManagementService;

    public JiraServiceController(IssueSearchService jiraService, IssueManagementService issueManagementService) {
        this.issueManagementService = issueManagementService;
        this.issueSearchService = jiraService;
    }

    @GetMapping("/search")
    public ResponseEntity<IssueSearchDTO.SearchResponse> searchIssues(
            @RequestParam String jql,
            @RequestParam(required = false) Integer maxResults) {

        logger.info("Searching Jira issues with JQL: {}", jql);
        try {
            IssueSearchDTO.SearchResponse issues = issueSearchService.searchIssues(jql, maxResults);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            logger.error("Error searching Jira issues: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/issue")
    public ResponseEntity<IssueManagementDTO.CreateIssueResponse> createIssue(
            @RequestBody IssueManagementDTO.CreateIssueRequest issueData) {

        logger.info("Creating new Jira issue");
        try {
            IssueManagementDTO.CreateIssueResponse result = issueManagementService.createIssue(issueData);
            if (result.id().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error creating Jira issue: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/issue/{issueKey}")
    public ResponseEntity<Void> updateIssue(
            @PathVariable String issueKey,
            @RequestBody IssueManagementDTO.CreateIssueRequest issueData) {

        logger.info("Updating Jira issue with key: {}", issueKey);
        try {
            ResponseEntity<Void> response = issueManagementService.updateIssue(issueKey, issueData);
            if (response.getStatusCode() != HttpStatus.NO_CONTENT) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error updating Jira issue: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/issue/{issueKey}/comment")
    public ResponseEntity<IssueManagementDTO.AddCommentResponse> addComment(
            @PathVariable String issueKey,
            @RequestBody String comment) {

        logger.info("Adding comment to Jira issue with key: {}", issueKey);
        try {
            IssueManagementDTO.AddCommentResponse result = issueManagementService.addComment(issueKey, comment);
            if (result.id().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            logger.error("Error adding comment to Jira issue: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
