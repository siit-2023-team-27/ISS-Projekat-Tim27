package DTO;

import model.Comment;
import model.User;

import java.util.List;

public class CommentDTO {
    private String title;
    private String text;
    private User user;
    private int rating; // Optional rating from 1 to 5

    public CommentDTO(){}
    public CommentDTO(String title, String text, User user, int rating, List<Comment> responses) {
        this.title = title;
        this.text = text;
        this.user = user;
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public int getRating() {
        return rating;
    }

}
