package com.mashup.challenge.github.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davi on 5/28/17.
 */
public class GitHubResponse {
    @JsonProperty("total_count")
    private int totalCount;

    private List<GitHubItem> items;

    public GitHubResponse() {
        this.items = new ArrayList<>();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GitHubItem> getItems() {
        return items;
    }

    public void setItems(List<GitHubItem> items) {
        this.items = items;
    }
}
