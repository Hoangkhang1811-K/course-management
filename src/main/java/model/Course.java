package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Course {
    private int courseId;
    private int categoryId;
    private int createdBy;
    private String title;
    private String shortDescription;
    private String description;
    private String thumbnailUrl;
    private String level;
    private BigDecimal price;
    private int status;
    private Timestamp createdAt;

    public Course() {
    }

    public Course(int courseId, int categoryId, int createdBy, String title, String shortDescription,
                  String description, String thumbnailUrl, String level, BigDecimal price,
                  int status, Timestamp createdAt) {
        this.courseId = courseId;
        this.categoryId = categoryId;
        this.createdBy = createdBy;
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.level = level;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Course(int categoryId, int createdBy, String title, String shortDescription,
                  String description, String thumbnailUrl, String level, BigDecimal price, int status) {
        this.categoryId = categoryId;
        this.createdBy = createdBy;
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.level = level;
        this.price = price;
        this.status = status;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", categoryId=" + categoryId +
                ", createdBy=" + createdBy +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", level='" + level + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}