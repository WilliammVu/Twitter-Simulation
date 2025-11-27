package models;
import java.time.LocalDateTime;
import java.util.HashSet;

public class Tweet {
    private final int id;
    private User user;
    private LocalDateTime date;
    private String body;
    private HashSet<User> usersWhoLiked;

    public Tweet(int id, User user, LocalDateTime date, String body){
        this.id = id;
        this.user = user;
        this.date = date;
        this.body = body;
        this.usersWhoLiked = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        //override java's addressed-based comparison
        if (this == o) return true;
        if (!(o instanceof Tweet)) return false;
        return id == ((Tweet)o).id;
    }

    @Override
    public int hashCode() {
        //override java's addressed-based hashing
        return Integer.hashCode(id);
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

    public void setBody(String newBody){
        body = newBody;
    }

    public int getLikeCount(){
        return usersWhoLiked.size();
    }

    public void addLike(User account){
        if(usersWhoLiked.contains(account)) return;
        usersWhoLiked.add(account);
    }

    public void removeLike(User account){
        if(!usersWhoLiked.contains(account)) return;
        usersWhoLiked.remove(account);
    }
}