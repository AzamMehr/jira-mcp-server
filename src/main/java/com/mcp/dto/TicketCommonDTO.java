package com.mcp.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public class TicketCommonDTO {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Project(
            @JsonProperty("key") String key
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IssueType(
            @JsonProperty("name") String name
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TextContent(
            @JsonProperty("type") String type,
            @JsonProperty("text") String text
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ParagraphContent(
            @JsonProperty("type") String type,
            @JsonProperty("content") List<TextContent> content
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Description(
            @JsonProperty("type") String type,
            @JsonProperty("version") int version,
            @JsonProperty("content") List<ParagraphContent> content
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Fields(
            @JsonProperty("project") Project project,
            @JsonProperty("summary") String summary,
            @JsonProperty("description") Description description,
            @JsonProperty("issuetype") IssueType issuetype
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Status(
            @JsonProperty("name") String name
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record IssueFields(
            @JsonProperty("summary") String summary,
            @JsonProperty("status") Status status
    ) {}
}
