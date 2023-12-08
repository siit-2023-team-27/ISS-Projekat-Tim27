package DTO;

import model.DateRange;
import model.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.Date;

public class ReservationDTO {

    private long user;
    private long accommodation;
//    private LocalDate startDate;
//    private LocalDate finishDate;
    private DateRange dateRange;
    private int numGuests;
    private ReservationStatus status;

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    // Constructor
    public ReservationDTO(
            long user,
            long accommodation,
            DateRange dateRange,
            int numGuests,
            ReservationStatus status
    ) {
        this.user = user;
        this.accommodation = accommodation;
        this.dateRange = dateRange;
        this.numGuests = numGuests;
        this.status = status;
    }

    public ReservationDTO () {}



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
