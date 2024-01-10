package DTO;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import model.Accommodation;
import model.AppUser;

public class AccommodationRatingDTO {

    private String userName;
    private String text;
    private int rating;
    private Long id;

    public AccommodationRatingDTO() {}

    public AccommodationRatingDTO(String userName, String text, int rating, Long id) {
        this.userName = userName;
        this.text = text;
        this.rating = rating;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
