package model;

import java.sql.Timestamp;

public class Enrollment {
    private int enrollmentId;
    private int userId;
    private int courseId;
    private Timestamp enrolledAt;
    private String status;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, int userId, int courseId, Timestamp enrolledAt, String status) {
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
        this.status = status;
    }

    public Enrollment(int userId, int courseId, String status) {
        this.userId = userId;
        this.courseId = courseId;
        this.status = status;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", enrolledAt=" + enrolledAt +
                ", status='" + status + '\'' +
                '}';
    }
}