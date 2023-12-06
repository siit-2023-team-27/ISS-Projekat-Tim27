package model;

import model.enums.ReportStatus;
public class CommentReport {
    private long id;
    private AppUser reportingAppUser;
    private Comment reportedComment;
    private String reason;
    private ReportStatus reportStatus;

    public CommentReport(){}
    // Constructor
    public CommentReport(AppUser reportingAppUser, Comment reportedComment, String reason, ReportStatus reportStatus) {
        this.reportingAppUser = reportingAppUser;
        this.reportedComment = reportedComment;
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

    public Comment getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(Comment reportedComment) {
        this.reportedComment = reportedComment;
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

    public void copyValues(CommentReport comment){
        this.reportingAppUser = comment.reportingAppUser;
        this.reportedComment = comment.reportedComment;
        this.reason = comment.reason;
        this.reportStatus = comment.reportStatus;
    }
}
