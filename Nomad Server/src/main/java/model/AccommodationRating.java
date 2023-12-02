package model;

public class AccommodationRating extends Rating{
    private Accommodation accommodation;

    public AccommodationRating() {}

    public AccommodationRating(AppUser appUser, Accommodation accommodation, int rating) {
        super(rating, appUser);
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
