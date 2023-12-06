package model;

import model.enums.ReportStatus;
public class UserReport {
    private long id;

    private AppUser reportingAppUser;
    private AppUser reportedAppUser;
    private String reason;
    private ReportStatus reportStatus;



    // Constructor
    public UserReport(AppUser reportingAppUser, AppUser reportedComment, String reason, ReportStatus reportStatus) {
        this.reportingAppUser = reportingAppUser;
        this.reportedAppUser = reportedComment;
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
    public AppUser getReportingUser() {
        return reportingAppUser;
    }

    public void setReportingUser(AppUser reportingAppUser) {
        this.reportingAppUser = reportingAppUser;
    }

    public AppUser getReportedUser() {
        return reportedAppUser;
    }

    public void setReportedUser(AppUser reportedAppUser) {
        this.reportedAppUser = reportedAppUser;
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
