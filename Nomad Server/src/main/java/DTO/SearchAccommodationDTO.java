package DTO;

import model.Amenity;
import model.Comment;
import model.Rating;
import model.enums.AccommodationStatus;
import model.enums.AccommodationType;
import model.enums.ConfirmationType;
import model.enums.PriceType;

import java.util.List;

public class SearchAccommodationDTO extends AccommodationDTO{
    Double totalPrice;
    Double pricePerNight;
    double averageRating;

    public SearchAccommodationDTO(){}

    public SearchAccommodationDTO(long id, int minGuests, int maxGuests, String name, String description, String address, List<Amenity> amenities, List<String> images, List<Comment> comments, List<Rating> ratings, AccommodationStatus status, ConfirmationType confirmationType, AccommodationType accommodationType, PriceType priceType, double defaultPrice, int deadlineForCancellation, boolean verified, Double totalPrice, Double pricePerNight, double averageRating) {
        super(id, minGuests, maxGuests, name, description, address, amenities, images, comments, ratings, status, confirmationType, accommodationType, priceType, defaultPrice, deadlineForCancellation, verified);
        this.totalPrice = totalPrice;
        this.pricePerNight = pricePerNight;
        this.averageRating = averageRating;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int nights) {
        this.pricePerNight = totalPrice/nights;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating() {
        for(Rating rating: this.getRatings()){
            this.averageRating += rating.getRating();
        }
        this.averageRating = this.averageRating/this.getRatings().size();
    }
}
