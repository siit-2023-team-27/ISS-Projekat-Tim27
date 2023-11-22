package DTO;

import model.DateRange;
import model.enums.ReservationStatus;

public class ReservationDTO {

    private long id;
    private long user;
    private long accommodation;
    private DateRange dateRange;
    private int numGuests;
    private ReservationStatus status;

    // Constructor
    public ReservationDTO(
            long id,
            long user,
            long accommodation,
            DateRange dateRange,
            int numGuests,
            ReservationStatus status
    ) {
        this.id = id;
        this.user = user;
        this.accommodation = accommodation;
        this.dateRange = dateRange;
        this.numGuests = numGuests;
        this.status = status;
    }

    public ReservationDTO () {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(long accommodation) {
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
}
