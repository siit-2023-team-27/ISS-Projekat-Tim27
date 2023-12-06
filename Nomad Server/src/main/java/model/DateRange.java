package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public class DateRange {
    @Column (name = "start_date")
    private Date startDate;
    @Column(name = "finish_date")
    private Date finishDate;

    // Constructor
    public DateRange(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null || startDate.after(finishDate)) {
            throw new IllegalArgumentException("Invalid date range. Start date must be before the end date.");
        }
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public DateRange() {}

    public boolean overlaps(DateRange comparedDateRange) {
        return !this.finishDate.before(comparedDateRange.getStartDate()) && !this.startDate.after(comparedDateRange.getFinishDate());
    }

    // Getters for each attribute
    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }
}
