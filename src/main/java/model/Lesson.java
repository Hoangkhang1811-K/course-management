package model;

public class Lesson {
    private int lessonId;
    private int courseId;
    private String title;
    private int lessonOrder;
    private String contentType;
    private String contentValue;
    private int status;

    public Lesson() {
    }

    public Lesson(int lessonId, int courseId, String title, int lessonOrder, String contentType, String contentValue, int status) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.lessonOrder = lessonOrder;
        this.contentType = contentType;
        this.contentValue = contentValue;
        this.status = status;
    }

    public Lesson(int courseId, String title, int lessonOrder, String contentType, String contentValue, int status) {
        this.courseId = courseId;
        this.title = title;
        this.lessonOrder = lessonOrder;
        this.contentType = contentType;
        this.contentValue = contentValue;
        this.status = status;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLessonOrder() {
        return lessonOrder;
    }

    public void setLessonOrder(int lessonOrder) {
        this.lessonOrder = lessonOrder;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", lessonOrder=" + lessonOrder +
                ", contentType='" + contentType + '\'' +
                ", contentValue='" + contentValue + '\'' +
                ", status=" + status +
                '}';
    }
}