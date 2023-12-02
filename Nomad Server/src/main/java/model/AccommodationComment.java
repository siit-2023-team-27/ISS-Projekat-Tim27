package model;

public class AccommodationComment extends Comment{
    private Accommodation accommodation;

    public AccommodationComment() {}

    public AccommodationComment(String text, AppUser appUser, Accommodation accommodation) {
        super(text, appUser);
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
        return "AccommodationComment{" +
                "accommodation=" + accommodation +
                '}';
    }
}
