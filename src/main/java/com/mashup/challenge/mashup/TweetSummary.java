package com.mashup.challenge.mashup;

/**
 * Created by davi on 5/29/17.
 */
class TweetSummary {
    private long id;
    private String text;
    private String source;
    private String createdAt;

    public TweetSummary(long id, String text, String source, String createdAt) {
        this.id = id;
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
