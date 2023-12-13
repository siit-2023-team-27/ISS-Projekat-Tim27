package model;

import jakarta.persistence.*;
import model.enums.ReservationStatus;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne (fetch = FetchType.LAZY, cascade = {})
    private Guest guest;
    @ManyToOne(fetch = FetchType.LAZY)
    private Accommodation accommodation;

    @Embedded
    private DateRange dateRange;
    private int numGuests;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    // Enum to represent reservation status: pending, accepted, rejected

    // Constructor
    public Reservation(Guest guest, Accommodation accommodation, DateRange dateRange, int numGuests, ReservationStatus status) {
        this.guest = guest;
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
    public Guest getUser() {
        return guest;
    }

    public void setUser(Guest appUser) {
        this.guest = appUser;
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
        this.guest = reservation.guest;
        this.accommodation = reservation.accommodation;
        this.dateRange = reservation.dateRange;
        this.numGuests = reservation.numGuests;
        this.status = reservation.status;
    }
}
