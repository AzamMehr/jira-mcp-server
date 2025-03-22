package com.mcp.app;

import com.mcp.service.TicketOperationsService;
import com.mcp.service.TicketQueryService;
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
    public ToolCallbackProvider jiraTools(TicketOperationsService ticketOperationsService, TicketQueryService ticketQueryService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(ticketOperationsService, ticketQueryService)
                .build();
    }
}
