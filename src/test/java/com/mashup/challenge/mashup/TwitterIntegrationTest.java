package com.mashup.challenge.mashup;

import com.mashup.challenge.github.response.GitHubItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by davi on 5/29/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration-test")
public class TwitterIntegrationTest {

    @Autowired
    private TwitterTemplate twitter;

    private MockRestServiceServer mockServer;

    @Autowired
    private MashupService mashupService;

    @Before
    public void setUp() throws Exception {
        mockServer = MockRestServiceServer.createServer(twitter.getRestTemplate());
    }

    @Test
    public void search() throws Exception {
        String keyword = "eclipse/vert.x";
        String url = "https://api.twitter.com/1.1/search/tweets.json?q=eclipse%2Fvert.x&result_type=recent&count=15";

        mockServer.expect(requestTo(url))
                .andExpect(method(GET))
                .andRespond(withSuccess(new ClassPathResource("twitter-search.json"), MediaType.APPLICATION_JSON));

        GitHubItem gitHubItem = new GitHubItem();
        gitHubItem.setId(12345L);
        gitHubItem.setUrl("https://github.com/" + keyword);
        gitHubItem.setFullName(keyword);
        MashupSummary summary = mashupService.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), hasSize(3));
    }
}
