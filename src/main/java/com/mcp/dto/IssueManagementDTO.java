package com.mcp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueManagementDTO {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CreateIssueRequest(
            @JsonProperty("fields") CommonDTO.Fields fields
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CreateIssueResponse(
            @JsonProperty("id") String id,
            @JsonProperty("key") String key
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UpdateIssueResponse(
            @JsonProperty("id") String id,
            @JsonProperty("key") String key,
            @JsonProperty("self") String self
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AddCommentResponse(
            @JsonProperty("id") String id,
            @JsonProperty("body") String body
    ) {}
}
