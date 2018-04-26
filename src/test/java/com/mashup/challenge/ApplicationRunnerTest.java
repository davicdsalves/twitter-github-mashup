package com.mashup.challenge;

import com.mashup.challenge.github.GitHubClient;
import com.mashup.challenge.github.response.GitHubItem;
import com.mashup.challenge.github.response.GitHubResponse;
import com.mashup.challenge.mashup.MashupService;
import com.mashup.challenge.mashup.MashupSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by davi on 5/29/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationRunnerTest {

    @Mock
    private GitHubClient gitHubClient;
    @Mock
    private MashupService mashupService;
    @InjectMocks
    private ApplicationRunner target;

    @Test
    public void shouldHaveNoRepositoriesFound() throws Exception {
        GitHubResponse githubResponse = new GitHubResponse();
        when(gitHubClient.search()).thenReturn(githubResponse);
        target.run();
        verify(mashupService, never()).searchProjectMentions(any(GitHubItem.class));
    }

    @Test
    public void shouldSearchForLessRepositoriesThenMax() throws Exception {
        int totalProjects = 5;
        int maxProjectsToSearch = 10;
        target.setMaxProjectsToSearch(maxProjectsToSearch);

        GitHubItem repository = new GitHubItem();
        List<GitHubItem> githubResult = Collections.nCopies(totalProjects, repository);
        GitHubResponse githubResponse = new GitHubResponse();
        githubResponse.setItems(githubResult);

        when(gitHubClient.search()).thenReturn(githubResponse);
        when(mashupService.searchProjectMentions(repository)).thenReturn(createMashupSummary());
        target.run();
        verify(mashupService, times(totalProjects)).searchProjectMentions(any(GitHubItem.class));
    }

    @Test
    public void shouldSearchForMaxRepositories() throws Exception {
        int totalProjects = 20;
        int maxProjectsToSearch = 10;
        target.setMaxProjectsToSearch(maxProjectsToSearch);

        GitHubItem repository = new GitHubItem();
        List<GitHubItem> githubResult = Collections.nCopies(totalProjects, repository);
        GitHubResponse githubResponse = new GitHubResponse();
        githubResponse.setItems(githubResult);

        when(gitHubClient.search()).thenReturn(githubResponse);
        when(mashupService.searchProjectMentions(repository)).thenReturn(createMashupSummary());
        target.run();
        verify(mashupService, times(maxProjectsToSearch)).searchProjectMentions(any(GitHubItem.class));
    }

    private MashupSummary createMashupSummary() {
        return new MashupSummary(1L, "githuburl", "projectname");
    }

}