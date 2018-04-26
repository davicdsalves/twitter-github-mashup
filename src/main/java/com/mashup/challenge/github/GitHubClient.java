package com.mashup.challenge.github;

import com.mashup.challenge.github.response.GitHubResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by davi on 5/28/17.
 */
@Component
public class GitHubClient {
    private Logger logger = LoggerFactory.getLogger(GitHubClient.class);

    private RestTemplate restTemplate;
    private URI searchURI;

    public GitHubClient(RestTemplate restTemplate, GitHubUrl gitHubUrl) {
        this.restTemplate = restTemplate;
        this.searchURI = gitHubUrl.createSearchURI();
    }

    public GitHubResponse search() {
        ResponseEntity<GitHubResponse> response = restTemplate.getForEntity(searchURI, GitHubResponse.class);
        GitHubResponse body = response.getBody();
        Object[] args = {searchURI, response.getStatusCodeValue(), body.getTotalCount(), body.getItems().size()};
        logger.info("GitHubSearchSuccess, url[{}], httpStatus[{}], totalCount[{}] listSize[{}]", args);
        return body;
    }
}
