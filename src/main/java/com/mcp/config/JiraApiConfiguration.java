package com.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "jira")
public record JiraApiConfiguration(String apiUrl, String username, String apiToken) {
}
