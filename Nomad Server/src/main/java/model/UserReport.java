package model;

import model.enums.ReportStatus;
public class UserReport {
    private long id;

    private User reportingUser;
    private User reportedUser;
    private String reason;
    private ReportStatus reportStatus;



    // Constructor
    public UserReport(User reportingUser, User reportedComment, String reason, ReportStatus reportStatus) {
        this.reportingUser = reportingUser;
        this.reportedUser = reportedComment;
        this.reason = reason;
        this.reportStatus = reportStatus;
    }

    // Getters and setters for each attribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(User reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public void copyValues(UserReport userReport) {
    }
}
