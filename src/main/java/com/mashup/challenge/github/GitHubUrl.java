package com.mashup.challenge.github;

import com.mashup.challenge.configuration.GitHubProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by davi on 5/28/17.
 */
@Component
public class GitHubUrl {
    private GitHubProperties properties;

    public GitHubUrl(GitHubProperties properties) {
        this.properties = properties;
    }

    URI createSearchURI() {
        GitHubProperties.Endpoint endpoint = properties.getEndpoint();

        return UriComponentsBuilder
                .fromHttpUrl(endpoint.getSearchUrl())
                .queryParam(endpoint.getParameterName(), endpoint.getSearchKeyword())
                .build().toUri();
    }
}
