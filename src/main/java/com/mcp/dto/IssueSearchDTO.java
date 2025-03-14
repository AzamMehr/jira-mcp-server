package com.mcp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IssueSearchDTO {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SearchResponse(
            @JsonProperty("issues") List<JiraIssue> issues
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record JiraIssue(
            @JsonProperty("id") String id,
            @JsonProperty("self") String self,
            @JsonProperty("key") String key,
            @JsonProperty("fields") CommonDTO.Fields fields
    ) {
        public String getSummary() {
            return fields != null ? fields.summary() : "";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GetIssueResponse(
            @JsonProperty("id") String id,
            @JsonProperty("key") String key,
            @JsonProperty("fields") CommonDTO.IssueFields fields
    ) {}
}
