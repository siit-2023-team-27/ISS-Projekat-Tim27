package model;

public class Rating {
    private long id;
    private int rating; // rating from 1 to 5
    private User user;

    public Rating() {}
    public Rating(int rating, User user) {
        this.rating = rating;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
