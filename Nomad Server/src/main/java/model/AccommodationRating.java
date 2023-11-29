package model;

public class AccommodationRating extends Rating{
    private Accommodation accommodation;

    public AccommodationRating() {}

    public AccommodationRating(User user, Accommodation accommodation, int rating) {
        super(rating, user);
        this.accommodation = accommodation;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public String toString() {
        return "AccommodationRating{" +
                "user=" + getUser() +
                ", accommodation=" + accommodation +
                ", rating=" + getRating() +
                '}';
    }
}
