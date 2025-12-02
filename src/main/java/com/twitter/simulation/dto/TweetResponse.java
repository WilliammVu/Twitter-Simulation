package com.twitter.simulation.dto;

import java.time.LocalDateTime;

public class TweetResponse {
    private int id;
    private String body;
    private UserResponse user;
    private LocalDateTime date;
    private int likeCount;
    private boolean isLiked;
    private boolean isRetweeted;

    public TweetResponse() {}

    public TweetResponse(int id, String body, UserResponse user, LocalDateTime date, int likeCount) {
        this.id = id;
        this.body = body;
        this.user = user;
        this.date = date;
        this.likeCount = likeCount;
        this.isLiked = false;
        this.isRetweeted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }
}
