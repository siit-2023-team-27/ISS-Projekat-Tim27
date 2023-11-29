package model;

public class HostRating extends Rating{
    private User host;

    public HostRating () {}

    public HostRating(int rating, User user, User host) {
        super(rating, user);
        this.host = host;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "HostRating{" +
                "host=" + host +
                '}';
    }

}
