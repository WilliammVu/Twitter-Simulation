package util;

import models.Tweet;
import java.util.HashMap;

//This class is used to store likes and retweets
//for ../models/User.java
public class OrderedTweetSet{
    private class Node{
        public Tweet tweet;
        public Node next;
        public Node prev;

        public Node(Tweet tweet){
            this.tweet = tweet;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;
    private HashMap<Tweet, Node> mp;

    public OrderedTweetSet(){
        mp = new HashMap<>();
        head = null;
        tail = null;
    }

    public void addTweet(Tweet tweet){
        //This would either be liking or retweeting a tweet
        if(mp.containsKey(tweet)) return;
        Node new_node = new Node(tweet);
        
        if(head == null){
            head = tail = new_node;
        }
        else{
            tail.next = new_node;
            new_node.prev = tail;
            tail = new_node;
        }

        mp.put(tweet, new_node);
    }

    public void removeTweet(Tweet tweet){
        //This would either be unliking or unretweeting a tweet
        if(!mp.containsKey(tweet)) return;
        Node target = mp.get(tweet);
        
        if(head == tail){
            head = tail = null;
        }
        else if(target == head){
            head.next.prev = null;
            head = head.next;
        }
        else if(target == tail){
            tail.prev.next = null;
            tail = tail.prev;
        }
        else{
            target.prev.next = target.next;
            target.next.prev = target.prev;
        }

        mp.remove(tweet);
    }

    public int size(){
        return mp.size();
    }
    
    public boolean isEmpty(){
        return head == null;
    }

    public Tweet[] getTweets(){
        int n = this.size();
        Tweet[] tweets = new Tweet[n];
        Node curr = head;
        for(int i=0;i<n;++i){
            tweets[i] = curr.tweet;
            curr = curr.next;
        }
        return tweets;
    }

    public boolean contains(Tweet tweet){
        return mp.containsKey(tweet);
    }
}