package com.twitter.simulation.dto;

public class TweetRequest {
    private String body;

    public TweetRequest() {}

    public TweetRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
