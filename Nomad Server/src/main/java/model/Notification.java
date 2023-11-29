package model;

public class Notification {
    private String text;
    private Long id;
    private User targetUser;
    private String title;

    public Notification(String text, User targetUser, String title) {
        this.text = text;
        this.targetUser = targetUser;
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

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
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
        this.targetUser = notification.targetUser;
    }
}
