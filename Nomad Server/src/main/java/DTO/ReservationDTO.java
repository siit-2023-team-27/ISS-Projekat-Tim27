package DTO;

import model.Accommodation;
import model.DateRange;
import model.User;
import model.enums.ReservationStatus;

public class ReservationDTO {

    private long id;
    private long userId;
    private long accommodationId;
    private DateRange dateRange;
    private int numGuests;
    private ReservationStatus status;

    // Constructor
    public ReservationDTO(
            long id,
            long userId,
            long accommodationId,
            DateRange dateRange,
            int numGuests,
            ReservationStatus status
    ) {
        this.id = id;
        this.userId = userId;
        this.accommodationId = accommodationId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(long accommodationId) {
        this.accommodationId = accommodationId;
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
