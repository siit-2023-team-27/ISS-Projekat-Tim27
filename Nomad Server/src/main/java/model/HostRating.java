package model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class HostRating extends Rating{
    @ManyToOne
    private AppUser host;

    public HostRating () {}

    public HostRating(int rating, AppUser appUser, AppUser host) {
        super(rating, appUser, "");
        this.host = host;
    }

    public AppUser getHost() {
        return host;
    }

    public void setHost(AppUser host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "HostRating{" +
                "host=" + host +
                '}';
    }

}
