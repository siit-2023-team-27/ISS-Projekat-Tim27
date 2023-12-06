package model;

import jakarta.persistence.*;

@Entity
public class Notification {
    private String text;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne (fetch = FetchType.EAGER)
    private AppUser targetAppUser;
    private String title;

    public Notification(String text, AppUser targetAppUser, String title) {
        this.text = text;
        this.targetAppUser = targetAppUser;
        this.title = title;
    }
    public Notification(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AppUser getTargetUser() {
        return targetAppUser;
    }

    public void setTargetUser(AppUser targetAppUser) {
        this.targetAppUser = targetAppUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void copyValues(Notification notification){
        this.title = notification.title;
        this.text = notification.text;
        this.targetAppUser = notification.targetAppUser;
    }
}
