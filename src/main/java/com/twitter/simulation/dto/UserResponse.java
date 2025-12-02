package com.twitter.simulation.dto;

public class UserResponse {
    private int id;
    private String username;
    private int followersCount;
    private int followingCount;
    private int tweetsCount;
    private boolean isFollowing;

    public UserResponse() {}

    public UserResponse(int id, String username, int followersCount, int followingCount, int tweetsCount) {
        this.id = id;
        this.username = username;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.tweetsCount = tweetsCount;
        this.isFollowing = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
