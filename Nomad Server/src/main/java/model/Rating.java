package model;

public class Rating {
    private long id;
    private int rating; // rating from 1 to 5
    private AppUser appUser;

    public Rating() {}
    public Rating(int rating, AppUser appUser) {
        this.rating = rating;
        this.appUser = appUser;
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

    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
