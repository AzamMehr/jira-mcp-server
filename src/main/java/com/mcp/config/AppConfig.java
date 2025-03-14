package com.mcp.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JiraApiConfiguration.class)
public class AppConfig {

    public AppConfig(JiraApiConfiguration jiraConfigProperties) {
    }

}