# Jira MCP Server

A Spring AI Model Context Protocol (MCP) server implementation that provides AI-powered tools for interacting with Jira. This project enables AI models to perform various Jira operations through a standardized protocol.

## Overview

This project implements a Spring AI MCP server that exposes Jira operations as AI tools. It provides capabilities for:
- Querying Jira tickets
- Creating new tickets
- Updating existing tickets
- Adding comments to tickets

The server follows the [Model Context Protocol (MCP)](https://docs.spring.io/spring-ai/reference/1.0/api/mcp/mcp-overview.html) specification, enabling standardized interaction between AI models and Jira operations.

## Features

- **Ticket Query Service**: Search and retrieve Jira tickets using JQL
- **Ticket Operations Service**: Create, update, and comment on Jira tickets
- Spring AI MCP server implementation
- RESTful API endpoints for Jira operations
- Secure authentication using Jira API tokens

## Prerequisites

- Java 21 or later
- Gradle 8.12.1 or later
- Jira instance with API access
- Jira API token

## Configuration

Configure the application using environment variables or application.properties:

```properties
# JIRA Configuration
jira.api-url=${JIRA_API_URL}
jira.username=${ALTASSIAN_USERNAME}
jira.api-token=${ALTASSIAN_TOKEN}

# Server Configuration
server.port=8081
```

## Building the Project

```bash
./gradlew build
```

## Running the Application

```bash
./gradlew bootRun
```

## Project Structure

```
src/main/java/com/mcp/
├── app/
│   └── JiraServiceApplication.java
├── config/
│   ├── AppConfig.java
│   └── JiraApiConfiguration.java
├── dto/
│   ├── CommonDTO.java
│   ├── IssueManagementDTO.java
│   └── IssueSearchDTO.java
└── service/
    ├── BaseJiraService.java
    ├── TicketOperationsService.java
    └── TicketQueryService.java
```

## Available Tools

### Ticket Query Service
- `searchIssues`: Search Jira tickets using JQL
- `getIssue`: Retrieve a specific ticket by key

### Ticket Operations Service
- `createIssue`: Create a new Jira ticket
- `updateIssue`: Update an existing ticket
- `addComment`: Add a comment to a ticket

## Dependencies

- Spring AI MCP Server WebFlux Starter
- Spring Boot
- Spring WebFlux
- Spring AI Core

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details. 