package DTO;

import model.enums.ReportStatus;

public class CommentReportDetailsDTO {
    private Long id;
    private Long reportingUser;
    private Long reportedUser;
    private String reason;
    private String reportingUserName;
    private String reportedUserName;
    private ReportStatus reportStatus;
    private String reportedCommentText;
    private int reportedCommentRating;

    public CommentReportDetailsDTO() {}

    public CommentReportDetailsDTO(Long id, Long reportingUser, Long reportedUser, String reason, String reportingUserName, String reportedUserName, ReportStatus reportStatus) {
        this.id = id;
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.reportingUserName = reportingUserName;
        this.reportedUserName = reportedUserName;
        this.reportStatus = reportStatus;
    }

    public String getReportedCommentText() {
        return reportedCommentText;
    }

    public void setReportedCommentText(String reportedCommentText) {
        this.reportedCommentText = reportedCommentText;
    }

    public int getReportedCommentRating() {
        return reportedCommentRating;
    }

    public void setReportedCommentRating(int reportedCommentRating) {
        this.reportedCommentRating = reportedCommentRating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportingUserName() {
        return reportingUserName;
    }

    public void setReportingUserName(String reportingUserName) {
        this.reportingUserName = reportingUserName;
    }

    public String getReportedUserName() {
        return reportedUserName;
    }

    public void setReportedUserName(String reportedUserName) {
        this.reportedUserName = reportedUserName;
    }

    public Long getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(Long reportingUser) {
        this.reportingUser = reportingUser;
    }

    public Long getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Long reportedUser) {
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
}
