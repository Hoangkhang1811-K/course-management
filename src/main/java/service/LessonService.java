package service;

import dao.LessonDAO;
import model.Lesson;

import java.util.Collections;
import java.util.List;

public class LessonService {
    private final LessonDAO lessonDAO;

    public LessonService() {
        this.lessonDAO = new LessonDAO();
    }

    // Lấy danh sách lesson theo course
    public List<Lesson> findByCourseId(int courseId) {
        if (courseId <= 0) {
            return Collections.emptyList();
        }
        return lessonDAO.findByCourseId(courseId);
    }

    // Lấy lesson theo id
    public Lesson findById(int lessonId) {
        if (lessonId <= 0) {
            return null;
        }
        return lessonDAO.findById(lessonId);
    }

    // Thêm lesson
    public boolean insertLesson(Lesson lesson) {
        if (!isValidLesson(lesson)) {
            return false;
        }

        normalizeLesson(lesson);
        return lessonDAO.insertLesson(lesson);
    }

    // Thêm lesson bằng tham số rời
    public boolean insertLesson(int courseId, String title, int lessonOrder,
                                String contentType, String contentValue, int status) {
        if (!isValidCourseId(courseId)
                || !isValidTitle(title)
                || !isValidLessonOrder(lessonOrder)
                || !isValidContentType(contentType)
                || !isValidContentValue(contentValue)
                || !isValidStatus(status)) {
            return false;
        }

        Lesson lesson = new Lesson(
                courseId,
                title.trim(),
                lessonOrder,
                normalizeContentType(contentType),
                contentValue.trim(),
                status
        );

        return lessonDAO.insertLesson(lesson);
    }

    // Update lesson
    public boolean updateLesson(Lesson lesson) {
        if (lesson == null || lesson.getLessonId() <= 0 || !isValidLesson(lesson)) {
            return false;
        }

        Lesson existingLesson = lessonDAO.findById(lesson.getLessonId());
        if (existingLesson == null) {
            return false;
        }

        normalizeLesson(lesson);
        return lessonDAO.updateLesson(lesson);
    }

    // Update lesson bằng tham số rời
    public boolean updateLesson(int lessonId, int courseId, String title, int lessonOrder,
                                String contentType, String contentValue, int status) {
        if (lessonId <= 0
                || !isValidCourseId(courseId)
                || !isValidTitle(title)
                || !isValidLessonOrder(lessonOrder)
                || !isValidContentType(contentType)
                || !isValidContentValue(contentValue)
                || !isValidStatus(status)) {
            return false;
        }

        Lesson existingLesson = lessonDAO.findById(lessonId);
        if (existingLesson == null) {
            return false;
        }

        Lesson lesson = new Lesson(
                lessonId,
                courseId,
                title.trim(),
                lessonOrder,
                normalizeContentType(contentType),
                contentValue.trim(),
                status
        );

        return lessonDAO.updateLesson(lesson);
    }

    // Xóa lesson
    public boolean deleteLesson(int lessonId) {
        if (lessonId <= 0) {
            return false;
        }

        Lesson existingLesson = lessonDAO.findById(lessonId);
        if (existingLesson == null) {
            return false;
        }

        return lessonDAO.deleteLesson(lessonId);
    }

    private boolean isValidLesson(Lesson lesson) {
        return lesson != null
                && isValidCourseId(lesson.getCourseId())
                && isValidTitle(lesson.getTitle())
                && isValidLessonOrder(lesson.getLessonOrder())
                && isValidContentType(lesson.getContentType())
                && isValidContentValue(lesson.getContentValue())
                && isValidStatus(lesson.getStatus());
    }

    private boolean isValidCourseId(int courseId) {
        return courseId > 0;
    }

    private boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.trim().length() <= 255;
    }

    private boolean isValidLessonOrder(int lessonOrder) {
        return lessonOrder > 0;
    }

    private boolean isValidContentType(String contentType) {
        return contentType != null && !contentType.trim().isEmpty();
    }

    private boolean isValidContentValue(String contentValue) {
        return contentValue != null && !contentValue.trim().isEmpty();
    }

    private boolean isValidStatus(int status) {
        return status == 0 || status == 1;
    }

    private String normalizeContentType(String contentType) {
        return contentType.trim().toUpperCase();
    }

    private void normalizeLesson(Lesson lesson) {
        lesson.setTitle(lesson.getTitle().trim());
        lesson.setContentType(normalizeContentType(lesson.getContentType()));
        lesson.setContentValue(lesson.getContentValue().trim());
    }
}