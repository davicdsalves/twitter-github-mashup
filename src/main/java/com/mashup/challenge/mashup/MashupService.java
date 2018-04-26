package com.mashup.challenge.mashup;

import com.mashup.challenge.github.response.GitHubItem;
import com.mashup.challenge.twitter.TwitterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.UrlEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Created by davi on 5/29/17.
 */
@Service
public class MashupService {
    private static final String GITHUB_URL = "github.com/%s";
    private Logger logger = LoggerFactory.getLogger(MashupService.class);

    private TwitterClient twitterClient;

    private DateTimeFormatter dateTimeFormatter;

    public MashupService(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
        this.dateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    }

    public MashupSummary searchProjectMentions(GitHubItem gitHubItem) {
        MashupSummary summary = createMashupSummary(gitHubItem);
        try {
            String keyword = gitHubItem.getFullName().toLowerCase();

            List<Tweet> projectTweets = twitterClient.searchRecentTweets(keyword);
            Set<Tweet> tweetsWithUrl = getTweetsWithUrl(projectTweets);

            tweetsWithUrl.forEach(tweet -> tweet.getEntities().getUrls().stream()
                    .filter(url -> isGitHubUrlOrTweetHasProjectName(keyword, tweet, url))
                    .findAny().ifPresent(urlEntity -> summary.getTweets().add(createTweetSummary(tweet))));

        } catch (RestClientException ex) {
            logger.error("Error searching for project mentions. project[{}]", gitHubItem, ex);
        }
        return summary;
    }

    private boolean isGitHubUrlOrTweetHasProjectName(String keyword, Tweet tweet, UrlEntity url) {
        String githubURL = String.format(GITHUB_URL, keyword);
        String relaxedMention = keyword.replace('/', ' ');
        return url.getExpandedUrl().toLowerCase().contains(githubURL) || tweet.getText().toLowerCase().contains(relaxedMention);
    }

    private Set<Tweet> getTweetsWithUrl(List<Tweet> projectTweets) {
        return projectTweets.stream().filter(p -> p.getEntities().hasUrls()).collect(Collectors.toSet());
    }

    private MashupSummary createMashupSummary(GitHubItem gitHubItem) {
        return new MashupSummary(gitHubItem.getId(), gitHubItem.getUrl(), gitHubItem.getFullName());
    }

    private TweetSummary createTweetSummary(Tweet tweet) {
        String tweetDate = dateTimeFormatter.format(tweet.getCreatedAt().toInstant());
        String tweetURL = String.format("https://twitter.com/%s/status/%d", tweet.getUser().getId(), tweet.getId());
        return new TweetSummary(tweet.getId(), tweet.getText(), tweetURL, tweetDate);
    }

}
