package com.mashup.challenge.mashup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davi on 5/29/17.
 */
public class MashupSummary {
    private Long githubID;
    private String githubURL;
    private String projectName;
    private List<TweetSummary> tweets;

    public MashupSummary(Long githubID, String githubURL, String projectName) {
        this.githubID = githubID;
        this.githubURL = githubURL;
        this.projectName = projectName;
        this.tweets = new ArrayList<>();
    }

    public Long getGithubID() {
        return githubID;
    }

    public void setGithubID(Long githubID) {
        this.githubID = githubID;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public void setGithubURL(String githubURL) {
        this.githubURL = githubURL;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TweetSummary> getTweets() {
        return tweets;
    }

    public void setTweets(List<TweetSummary> tweets) {
        this.tweets = tweets;
    }

}
