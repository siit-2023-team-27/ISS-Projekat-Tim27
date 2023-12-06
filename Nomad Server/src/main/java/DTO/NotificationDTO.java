package DTO;

public class NotificationDTO {
    private String text;
    private String title;
    private Long targetAppUser;

    public NotificationDTO(String text, String title, Long targetAppUser) {
        this.text = text;
        this.title = title;
        this.targetAppUser = targetAppUser;
    }
    public NotificationDTO(){}
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTargetAppUser() {
        return targetAppUser;
    }

    public void setTargetAppUser(Long targetAppUser) {
        this.targetAppUser = targetAppUser;
    }
}
