package com.mcp.app;

import com.mcp.service.IssueManagementService;
import com.mcp.service.IssueSearchService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mcp"})
public class JiraServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiraServiceApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider jiraTools(IssueManagementService issueManagementService, IssueSearchService issueSearchService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(issueManagementService, issueSearchService)
                .build();
    }
}
