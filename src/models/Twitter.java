package models;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Twitter {
    private Map<String, User> usersByUsername;
    private Map<Integer, User> usersById;
    private Map<Integer, Tweet> tweetsById;
    private User currentUser; //not sure if we want to deal with this here
    private int nextUserId;
    private int nextTweetId;

    public Twitter() {
        this.usersByUsername = new HashMap<>();
        this.usersById = new HashMap<>();
        this.tweetsById = new HashMap<>();
        this.currentUser = null;
        this.nextUserId = 1;
        this.nextTweetId = 1;
    }
}