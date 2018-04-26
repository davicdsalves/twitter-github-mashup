package com.mashup.challenge.github;

import com.mashup.challenge.github.response.GitHubResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by davi on 5/28/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class GitHubIntegrationTest {

    private MockRestServiceServer github;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GitHubClient gitHubClient;

    @Autowired
    private GitHubUrl gitHubUrl;

    @Before
    public void setUp() throws Exception {
        this.github = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void search() throws Exception {
        github.expect(requestTo(gitHubUrl.createSearchURI()))
                .andRespond(withSuccess(new ClassPathResource("github-reactive-query.json"), MediaType.APPLICATION_JSON));
        GitHubResponse response = gitHubClient.search();
        assertThat(response.getTotalCount(), is(equalTo(9098)));
        assertThat(response.getItems(), hasSize(30));
    }

}