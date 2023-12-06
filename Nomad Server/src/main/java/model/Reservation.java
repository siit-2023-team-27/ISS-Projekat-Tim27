package model;

import model.enums.ReservationStatus;

public class Reservation {
    private long id;

    private AppUser appUser;
    private Accommodation accommodation;
    private DateRange dateRange;
    private int numGuests;
    private ReservationStatus status;

    // Enum to represent reservation status: pending, accepted, rejected

    // Constructor
    public Reservation(AppUser appUser, Accommodation accommodation, DateRange dateRange, int numGuests, ReservationStatus status) {
        this.appUser = appUser;
        this.accommodation = accommodation;
        this.dateRange = dateRange;
        this.numGuests = numGuests;
        this.status = status;
    }

    public Reservation() {}

    // Getters and setters for each attribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void copyValues(Reservation reservation) {
        this.id = reservation.id;
        this.appUser = reservation.appUser;
        this.accommodation = reservation.accommodation;
        this.dateRange = reservation.dateRange;
        this.numGuests = reservation.numGuests;
        this.status = reservation.status;
    }
}
