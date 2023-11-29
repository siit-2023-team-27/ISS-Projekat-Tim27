package model;

public class HostComment extends Comment{
    private User host;

    public HostComment() {}

    public HostComment(String text, User user, User host) {
        super(text, user);
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
        return "HostComment{" +
                "host=" + host +
                '}';
    }
}
