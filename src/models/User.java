package models;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeSet;

public class User{
    private int id;
    private String username;
    private String password;
    private HashSet<User> followers;
    private HashSet<User> following;
    private ArrayList<Tweet> tweets;
    private TreeSet<Tweet> likes;
    private TreeSet<Tweet> retweets;
    

    public User(int id, String username, String password){
        this.id = id; this.username = username; this.password = password;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.tweets = new ArrayList<>();
    }

    public int getID(){return this.id;}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public HashSet<User> getFollowers(){return this.followers;}
    public HashSet<User> getFollowing(){return this.following;}
    public ArrayList<Tweet> getTweets(){return this.tweets;}

    public void editUsername(String other){
        this.username = other;
    }
    public void editPassword(String password){
        this.password = password;
    }
    public void follow(User other_account){
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
        this.tweets.add(new_tweet);
    }
    public void like(Tweet tweet){
        if(this.likes.contains(tweet)) return;
        this.likes.add(tweet);
    }
    public void unlike(Tweet tweet){
        if(!this.likes.contains(tweet)) return;
        this.likes.remove(tweet);
    }
    public void retweet(Tweet tweet){
        if(this.retweets.contains(tweet)) return;
        this.retweets.add(tweet);
    }
    public void unRetweet(Tweet tweet){
        if(!this.retweets.contains(tweet)) return;
        this.retweets.remove(tweet);
    }
}