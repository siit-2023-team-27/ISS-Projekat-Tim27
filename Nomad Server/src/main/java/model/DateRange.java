package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.util.Date;

@Embeddable
public class DateRange {
    @Column (name = "start_date")
    private LocalDate startDate;
    @Column(name = "finish_date")
    private LocalDate finishDate;

    // Constructor
    public DateRange(LocalDate startDate, LocalDate finishDate) {
        if (startDate == null || finishDate == null || startDate.isAfter(finishDate)) {
            throw new IllegalArgumentException("Invalid date range. Start date must be before the end date.");
        }
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public DateRange() {}

    public boolean overlaps(DateRange comparedDateRange) {
        return !this.finishDate.isBefore(comparedDateRange.getStartDate()) && !this.startDate.isAfter(comparedDateRange.getFinishDate());
    }

    // Getters for each attribute
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }
}
