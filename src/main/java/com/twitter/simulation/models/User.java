package com.twitter.simulation.models;

import java.util.HashSet;

import com.twitter.simulation.util.OrderedTweetSet;

public class User{
    private final int id;
    private String username;
    private String password;
    private HashSet<User> followers;
    private HashSet<User> following;
    private OrderedTweetSet tweets;
    private OrderedTweetSet likes;
    private OrderedTweetSet retweets;
    
    public User(int id, String username, String password){
        this.id = id; this.username = username; this.password = password;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.tweets = new OrderedTweetSet();
        this.likes = new OrderedTweetSet();
        this.retweets = new OrderedTweetSet();
    }

    @Override
    public boolean equals(Object o) {
        //override java's address based comparison
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id == ((User)o).id;
    }

    @Override
    public int hashCode() {
        //override java's address hashing
        return Integer.hashCode(id);
    }

    public int getID(){return this.id;}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public HashSet<User> getFollowers(){return new HashSet<>(followers);}
    public HashSet<User> getFollowing(){return new HashSet<>(following);}
    public Tweet[] getTweets(){return this.tweets.getTweets();}
    public Tweet[] getLikes(){return this.likes.getTweets();}
    public Tweet[] getRetweets(){return this.retweets.getTweets();}

    public void editUsername(String other){
        this.username = other;
    }

    public void editPassword(String password){
        this.password = password;
    }

    public void follow(User other_account){
        if(other_account == this) return;
        if(following.contains(other_account)) return;

        following.add(other_account);
        other_account.followers.add(this);
    }

    public void unfollow(User other_account){
        if(!this.following.contains(other_account)){
            return;
        }
        this.following.remove(other_account);
        other_account.followers.remove(this);
    }

    public void post(Tweet new_tweet){
        if(new_tweet.getUser() != this){
            throw new IllegalArgumentException("Tweet must belong to this user");
        }
        this.tweets.addTweet(new_tweet);
    }

    public void like(Tweet tweet){
        if(this.likes.contains(tweet)) return;
        this.likes.addTweet(tweet);
        tweet.addLike(this);
    }

    public void unlike(Tweet tweet){
        if(!this.likes.contains(tweet)) return;
        this.likes.removeTweet(tweet);
        tweet.removeLike(this);
    }

    public void retweet(Tweet tweet){
        if(this.retweets.contains(tweet)) return;
        this.retweets.addTweet(tweet);
    }

    public void unretweet(Tweet tweet){
        if(!this.retweets.contains(tweet)) return;
        this.retweets.removeTweet(tweet);
    }
}