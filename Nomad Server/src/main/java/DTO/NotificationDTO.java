package DTO;

public class NotificationDTO {
    private String text;
    private String title;

    public NotificationDTO(String text, String title) {
        this.text = text;
        this.title = title;
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
}
