package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Queue;

public class Twitter {
    private HashMap<String, User> usersByUsername;
    private HashMap<Integer, User> usersById;
    private HashMap<Integer, Tweet> tweetsById;
    private User currentUser;
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

    //--------------------------------------------
    // USER AUTHENTICATION
    //--------------------------------------------

    public boolean login(String username, String password){
        StringBuilder un = new StringBuilder();
        for(char c : username.toCharArray()){
            un.append(Character.toLowerCase(c));
        } username = un.toString();
        User user = usersByUsername.get(username);
        if(user == null || user.getPassword().equals(password)){
            return false;
        }
        currentUser = user;
        return true;
    }

    public void logout(){
        currentUser = null;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    private boolean validPassword(String password){
        if(password == null) return false;
        int len = password.length();
        if(len < 6 || len > 16) return false;

        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for(char c : password.toCharArray()){
            if(Character.isDigit(c)) hasDigit = true;
            else if(!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }
        
        return hasDigit && hasSpecialChar;
    }

    private boolean validUsername(String username){
        if(username == null || username.isEmpty()) return false;
        
        int len = username.length();
        if(len < 3 || len > 20) return false;
        
        // First character must be a letter
        if(!Character.isLetter(username.charAt(0))) return false;
        
        // Check valid characters: letters, digits, underscore, hyphen
        for(char c : username.toCharArray()){
            if(!Character.isLetterOrDigit(c) && c != '_' && c != '-'){
                return false;
            }
        }
        
        // No consecutive special chars
        if(username.contains("__") || username.contains("--") || 
        username.contains("_-") || username.contains("-_")){
            return false;
        }
        
        // Can't end with special char
        char lastChar = username.charAt(len - 1);
        if(lastChar == '_' || lastChar == '-'){
            return false;
        }
        
        return true;
    }

    public boolean createUser(String username, String password){
        if(usersByUsername.containsKey(username)) return false;
        if(!validPassword(password) || !validUsername(username)) return false; 

        User newUser = new User(nextUserId++, username, password);
        usersByUsername.put(username, newUser);
        usersById.put(newUser.getID(), newUser);

        return true;
    }

    //--------------------------------------------
    // USER MANAGEMENT
    //--------------------------------------------

    public User getUserByUsername(String username){
        return usersByUsername.get(username);
    }

    public User getUserById(int id){
        return usersById.get(id);
    }

    //--------------------------------------------
    // FOLLOWING
    //--------------------------------------------

    public void follow(User follower, User followee) {
        if (follower == null || followee == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }
        follower.follow(followee);
    }

    public void unfollow(User follower, User followee) {
        if (follower == null || followee == null) {
            throw new IllegalArgumentException("Users cannot be null");
        }
        follower.unfollow(followee);
    }

    //--------------------------------------------
    // TWEET MANAGEMENT
    //--------------------------------------------

    public Tweet postTweet(User user, String body) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Tweet body cannot be empty");
        }
        
        Tweet tweet = new Tweet(nextTweetId++, user, LocalDateTime.now(), body);
        tweetsById.put(tweet.getID(), tweet);
        user.post(tweet);
        return tweet;
    }

    public Tweet getTweet(int tweetId) {
        return tweetsById.get(tweetId);
    }

    public void likeTweet(User user, Tweet tweet) {
        if (user == null || tweet == null) {
            throw new IllegalArgumentException("User and tweet cannot be null");
        }
        user.like(tweet);
    }

    public void unlikeTweet(User user, Tweet tweet) {
        if (user == null || tweet == null) {
            throw new IllegalArgumentException("User and tweet cannot be null");
        }
        user.unlike(tweet);
    }

    public void retweet(User user, Tweet tweet) {
        if (user == null || tweet == null) {
            throw new IllegalArgumentException("User and tweet cannot be null");
        }
        user.retweet(tweet);
    }

    public void unretweet(User user, Tweet tweet) {
        if (user == null || tweet == null) {
            throw new IllegalArgumentException("User and tweet cannot be null");
        }
        user.unretweet(tweet);
    }

    //--------------------------------------------
    // MORE FEATURES
    //--------------------------------------------

    public User search(String query){
        if(query == null || query.empty()) return;

        String lowercaseQuery = query.toLowerCase();

        return usersByUsername.get(lowercaseQuery);
    }

    public Tweet[] getFeed(User user){
        PriorityQueue<Tweet> pq = new PriorityQueue<>(
            (a, b) -> b.getDate().compareTo(a.getDate())
        );

        for(User followedUser : user.getFollowing()){
            for(Tweet twt : followedUser.getTweets){
                pq.push
            }
        }
    }
}