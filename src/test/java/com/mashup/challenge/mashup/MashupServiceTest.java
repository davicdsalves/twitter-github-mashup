package com.mashup.challenge.mashup;

import com.mashup.challenge.github.response.GitHubItem;
import com.mashup.challenge.twitter.TwitterClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UrlEntity;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by davi on 5/29/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MashupServiceTest {
    @Mock
    private TwitterClient twitterClient;
    @InjectMocks
    private MashupService target;

    private String repo = "org/repo";

    @Test
    public void shouldFailForTwitterSearch() throws Exception {
        when(twitterClient.searchRecentTweets(repo)).thenThrow(new RestClientException("Error acessing Twitter"));
        GitHubItem gitHubItem = createGitHubItem();
        MashupSummary summary = target.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), empty());
    }

    @Test
    public void shouldNotFindAnyTweets() throws Exception {
        when(twitterClient.searchRecentTweets(repo)).thenReturn(Collections.emptyList());
        GitHubItem gitHubItem = createGitHubItem();
        MashupSummary summary = target.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), empty());
    }

    @Test
    public void shouldNotFindAnyTweetsWithUrl() throws Exception {
        Tweet tweet = mock(Tweet.class);
        Entities entities = mock(Entities.class);
        when(entities.hasUrls()).thenReturn(false);
        when(tweet.getEntities()).thenReturn(entities);
        when(twitterClient.searchRecentTweets(repo)).thenReturn(Collections.singletonList(tweet));

        GitHubItem gitHubItem = createGitHubItem();
        MashupSummary summary = target.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), empty());
    }

    @Test
    public void shouldNotFindAnyTweetsByUrl() throws Exception {
        Tweet tweet = mock(Tweet.class);
        Entities entities = mock(Entities.class);
        when(entities.hasUrls()).thenReturn(true);

        List<UrlEntity> urlEntities = new ArrayList<>();
        urlEntities.add(createUrlEntity("org/anotherRepo"));
        when(entities.getUrls()).thenReturn(urlEntities);
        when(tweet.getEntities()).thenReturn(entities);
        when(tweet.getText()).thenReturn("tweet text");
        when(twitterClient.searchRecentTweets(repo)).thenReturn(Collections.singletonList(tweet));

        GitHubItem gitHubItem = createGitHubItem();
        MashupSummary summary = target.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), empty());
    }

    @Test
    public void shouldFindTweetsByUrl() throws Exception {
        Tweet tweet = mock(Tweet.class);
        Entities entities = mock(Entities.class);
        when(entities.hasUrls()).thenReturn(true);

        List<UrlEntity> urlEntities = new ArrayList<>();
        urlEntities.add(createUrlEntity("github.com/" + repo));
        when(entities.getUrls()).thenReturn(urlEntities);
        when(tweet.getEntities()).thenReturn(entities);
        when(tweet.getText()).thenReturn("tweet text");
        when(tweet.getCreatedAt()).thenReturn(new Date());
        TwitterProfile twitterProfile = mock(TwitterProfile.class);
        when(tweet.getUser()).thenReturn(twitterProfile);
        when(twitterClient.searchRecentTweets(repo)).thenReturn(Collections.singletonList(tweet));

        GitHubItem gitHubItem = createGitHubItem();
        MashupSummary summary = target.searchProjectMentions(gitHubItem);
        assertThat(summary.getTweets(), hasSize(1));
    }


    private UrlEntity createUrlEntity(String expandedURL) {
        int[] indices = {0, 1};
        return new UrlEntity("display", expandedURL, "url", indices);
    }

    private GitHubItem createGitHubItem() {
        GitHubItem gitHubItem = new GitHubItem();
        gitHubItem.setFullName(repo);
        return gitHubItem;
    }

}