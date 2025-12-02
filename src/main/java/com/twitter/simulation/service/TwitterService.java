package com.twitter.simulation.service;

import com.twitter.simulation.dto.TweetResponse;
import com.twitter.simulation.dto.UserResponse;
import com.twitter.simulation.models.Tweet;
import com.twitter.simulation.models.Twitter;
import com.twitter.simulation.models.User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterService {

    private final Twitter twitter;

    public TwitterService() {
        this.twitter = new Twitter();
    }

    public Twitter getTwitterInstance() {
        return twitter;
    }

    // Authentication methods
    public boolean login(String username, String password) {
        return twitter.login(username, password);
    }

    public void logout() {
        twitter.logout();
    }

    public User getCurrentUser() {
        return twitter.getCurrentUser();
    }

    public boolean createUser(String username, String password) {
        return twitter.createUser(username, password);
    }

    // User management
    public User getUserByUsername(String username) {
        return twitter.getUserByUsername(username);
    }

    public User getUserById(int id) {
        return twitter.getUserById(id);
    }

    public User search(String query) {
        return twitter.search(query);
    }

    // Following
    public void follow(User follower, User followee) {
        twitter.follow(follower, followee);
    }

    public void unfollow(User follower, User followee) {
        twitter.unfollow(follower, followee);
    }

    // Tweet management
    public Tweet postTweet(User user, String body) {
        return twitter.postTweet(user, body);
    }

    public Tweet getTweet(int tweetId) {
        return twitter.getTweet(tweetId);
    }

    public void likeTweet(User user, Tweet tweet) {
        twitter.likeTweet(user, tweet);
    }

    public void unlikeTweet(User user, Tweet tweet) {
        twitter.unlikeTweet(user, tweet);
    }

    public void retweet(User user, Tweet tweet) {
        twitter.retweet(user, tweet);
    }

    public void unretweet(User user, Tweet tweet) {
        twitter.unretweet(user, tweet);
    }

    public Tweet[] getFeed(User user) {
        return twitter.getFeed(user);
    }

    public User[] getSuggestedFriends(User user) {
        return twitter.getSuggestedFriends(user);
    }

    // Conversion methods
    public UserResponse convertToUserResponse(User user, User currentUser) {
        if (user == null) return null;

        UserResponse response = new UserResponse(
            user.getID(),
            user.getUsername(),
            user.getFollowers().size(),
            user.getFollowing().size(),
            user.getTweets().length
        );

        if (currentUser != null) {
            response.setFollowing(currentUser.getFollowing().contains(user));
        }

        return response;
    }

    public TweetResponse convertToTweetResponse(Tweet tweet, User currentUser) {
        if (tweet == null) return null;

        UserResponse userResponse = convertToUserResponse(tweet.getUser(), currentUser);

        TweetResponse response = new TweetResponse(
            tweet.getID(),
            tweet.getBody(),
            userResponse,
            tweet.getDate(),
            tweet.getLikeCount()
        );

        if (currentUser != null) {
            List<Tweet> likedTweets = Arrays.asList(currentUser.getLikes());
            List<Tweet> retweetedTweets = Arrays.asList(currentUser.getRetweets());

            response.setLiked(likedTweets.contains(tweet));
            response.setRetweeted(retweetedTweets.contains(tweet));
        }

        return response;
    }

    public List<UserResponse> convertToUserResponseList(User[] users, User currentUser) {
        return Arrays.stream(users)
                .map(user -> convertToUserResponse(user, currentUser))
                .collect(Collectors.toList());
    }

    public List<TweetResponse> convertToTweetResponseList(Tweet[] tweets, User currentUser) {
        return Arrays.stream(tweets)
                .map(tweet -> convertToTweetResponse(tweet, currentUser))
                .collect(Collectors.toList());
    }
}
