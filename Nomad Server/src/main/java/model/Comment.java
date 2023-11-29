package model;

import java.util.ArrayList;
import java.util.List;

public class Comment {

    private long id;
    private String text;
    private User user;

    public Comment(){}

    public Comment(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user=" + user +
                '}';
    }

    public void copyValues(Comment comment){
        this.text = comment.text;
        this.user = comment.user;
    }
}