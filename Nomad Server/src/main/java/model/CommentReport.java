package model;

import model.enums.ReportStatus;
public class CommentReport {
    private User reportingUser;
    private Comment reportedComment;
    private String reason;
    private ReportStatus reportStatus;


    // Constructor
    public CommentReport(User reportingUser, Comment reportedComment, String reason, ReportStatus reportStatus) {
        this.reportingUser = reportingUser;
        this.reportedComment = reportedComment;
        this.reason = reason;
        this.reportStatus = reportStatus;
    }

    // Getters and setters for each attribute
    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
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

}
