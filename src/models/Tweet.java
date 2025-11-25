package models;
import java.time.LocalDateTime;

public class Tweet {
    private int id;
    private User user;
    private LocalDateTime date;
    private String body;

    public Tweet(int id, User user, LocalDateTime date, String body){
        this.id = id;
        this.user = user;
        this.date = date;
        this.body = body;
    }

    public int getID(){
        return this.id;
    }

    public User getUser(){
        return user;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public String getBody(){
        return this.body;
    }

    public void editBody(String newBody){
        body = newBody;
    }
}