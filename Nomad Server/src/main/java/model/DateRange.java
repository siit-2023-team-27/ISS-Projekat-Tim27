package model;

import java.util.Date;

public class DateRange {
    private final Date startDate;
    private final Date finishDate;

    // Constructor
    public DateRange(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null || startDate.after(finishDate)) {
            throw new IllegalArgumentException("Invalid date range. Start date must be before the end date.");
        }
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
    public boolean overlaps(DateRange comparedDateRange) {
        return !this.finishDate.before(comparedDateRange.getStartDate()) && !this.startDate.after(comparedDateRange.getFinishDate());
    }

    // Getters and setters for each attribute
    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }


}
