package model;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String title;
    private String text;
    private User user;
    private int rating; // Optional rating from 1 to 5
    private List<Comment> responses;

    // Constructor without a rating
    public Comment(String title, String text, User user) {
        this.title = title;
        this.text = text;
        this.user = user;
        this.responses = new ArrayList<>();
    }

    // Constructor with a rating
    public Comment(String title, String text, User user, int rating) {
        this(title, text, user);
        this.rating = (rating >= 1 && rating <= 5) ? rating : 0;
    }

    // Getters and setters for each attribute
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = (rating >= 1 && rating <= 5) ? rating : 0;
    }

    public List<Comment> getResponses() {
        return responses;
    }

    public void setResponses(List<Comment> responses) {
        this.responses = responses;
    }

    // Method to add a response comment
    public void addResponse(Comment response) {
        this.responses.add(response);
    }

    // toString method to represent the comment as a string
    @Override
    public String toString() {
        return "Comment{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", user=" + user +
                ", rating=" + rating +
                ", responses=" + responses +
                '}';
    }
}