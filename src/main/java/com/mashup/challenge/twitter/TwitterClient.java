package com.mashup.challenge.twitter;

import com.mashup.challenge.configuration.TwitterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by davi on 5/28/17.
 */
@Component
public class TwitterClient {
    private Logger logger = LoggerFactory.getLogger(TwitterClient.class);

    private TwitterTemplate twitter;
    private TwitterProperties properties;

    public TwitterClient(TwitterTemplate twitter, TwitterProperties properties) {
        this.twitter = twitter;
        this.properties = properties;
    }

    public List<Tweet> searchRecentTweets(String keyword) {
        SearchParameters searchParameters = createSearchParameters(keyword);
        List<Tweet> projectTweets = twitter.searchOperations().search(searchParameters).getTweets();
        logger.debug("SearchTwitterSuccess, keyword[{}], tweetsCount[{}]", keyword, projectTweets.size());
        return projectTweets;
    }

    private SearchParameters createSearchParameters(String keyword) {
        return new SearchParameters(keyword)
                    .includeEntities(true)
                    .resultType(SearchParameters.ResultType.RECENT)
                    .count(properties.getSearchPageSize());
    }

}
