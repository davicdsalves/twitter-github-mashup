package com.mashup.challenge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashup.challenge.github.GitHubClient;
import com.mashup.challenge.github.response.GitHubItem;
import com.mashup.challenge.github.response.GitHubResponse;
import com.mashup.challenge.mashup.MashupService;
import com.mashup.challenge.mashup.MashupSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

/**
 * Created by davi on 5/28/17.
 */
@Component
@Profile("!integration-test")
public class ApplicationRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    private GitHubClient gitHubClient;
    private MashupService mashupService;
    private ObjectMapper objectMapper;

    private int maxProjectsToSearch;
    private boolean randomizeRepositories;

    public ApplicationRunner(GitHubClient gitHubClient, MashupService mashupService) {
        this.gitHubClient = gitHubClient;
        this.mashupService = mashupService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            GitHubResponse response = gitHubClient.search();
            List<GitHubItem> repositories = response.getItems();

            if (!repositories.isEmpty()) {
                if (randomizeRepositories) {
                    Collections.shuffle(repositories);
                }

                int lastIndex = (repositories.size() < maxProjectsToSearch ? repositories.size() : maxProjectsToSearch);
                repositories.subList(0, lastIndex).forEach(gitHubItem -> {
                    MashupSummary summary = mashupService.searchProjectMentions(gitHubItem);
                    printSummary(summary);
                });
            } else {
                logger.warn("No repositories found.");
            }
        } catch (RestClientException ex) {
            logger.error("Error searching for project on github.", ex);
        }
    }

    private void printSummary(MashupSummary summary) {
        try {
            String json = objectMapper.writeValueAsString(summary);
            logger.info(json);
        } catch (JsonProcessingException e) {
            logger.error("Error writing summary to json, summary[{}]", summary, e);
        }
    }

    @Value("${twitter.max-projects}")
    public void setMaxProjectsToSearch(int maxProjectsToSearch) {
        this.maxProjectsToSearch = maxProjectsToSearch;
    }

    @Value("${github.randomize-repositories}")
    public void setRandomizeRepositories(boolean randomizeRepositories) {
        this.randomizeRepositories = randomizeRepositories;
    }
}
