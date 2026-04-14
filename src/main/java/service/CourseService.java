package service;

import dao.CourseDAO;
import model.Course;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseService {
    private static final int ACTIVE_STATUS = 1;

    private final CourseDAO courseDAO;

    public CourseService() {
        courseDAO = new CourseDAO();
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public Course getCourseById(int courseId) {
        if (courseId <= 0) {
            return null;
        }
        return courseDAO.findById(courseId);
    }

    public List<Course> getActiveCourses() {
        return filterActiveCourses(courseDAO.findAll());
    }

    public List<Course> getCoursesByCategory(int categoryId) {
        if (categoryId <= 0) {
            return new ArrayList<>();
        }
        return courseDAO.findByCategory(categoryId);
    }

    public List<Course> getActiveCoursesByCategory(int categoryId) {
        return filterActiveCourses(getCoursesByCategory(categoryId));
    }

    public List<Course> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return courseDAO.findAll();
        }
        return courseDAO.searchByTitle(keyword.trim());
    }

    public List<Course> searchActiveByTitle(String keyword) {
        return filterActiveCourses(searchByTitle(keyword));
    }

    public boolean createCourse(Course course) {
        if (!isValidCourse(course)) {
            return false;
        }
        normaliseCourse(course);
        return courseDAO.insertCourse(course);
    }

    public boolean updateCourse(Course course) {
        if (course == null || course.getCourseId() <= 0) {
            return false;
        }

        if (!isValidCourse(course)) {
            return false;
        }
        normaliseCourse(course);
        return courseDAO.updateCourse(course);
    }

    public boolean deleteCourse(int courseId) {
        if (courseId <= 0) {
            return false;
        }
        return courseDAO.deleteCourse(courseId);
    }

    public List<Course> filterActiveCourses(List<Course> courses) {
        List<Course> activeCourses = new ArrayList<>();

        if (courses == null || courses.isEmpty()) {
            return activeCourses;
        }

        for (Course course : courses) {
            if (course != null && course.getStatus() == ACTIVE_STATUS) {
                activeCourses.add(course);
            }
        }
        return activeCourses;
    }

    private boolean isValidCourse(Course course) {
        if (course == null) {
            return false;
        }

        if (course.getCategoryId() <= 0) {
            return false;
        }

        if (course.getCreatedBy() <= 0) {
            return false;
        }

        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            return false;
        }

        if (course.getTitle().trim().length() > 150) {
            return false;
        }

        if (course.getShortDescription() != null && course.getShortDescription().length() > 255) {
            return false;
        }

        if (course.getLevel() != null && course.getLevel().trim().length() > 50) {
            return false;
        }

        if (course.getPrice() != null && course.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        if (course.getStatus() != 0 && course.getStatus() != 1) {
            return false;
        }

        return true;
    }

    private void normaliseCourse(Course course) {
        course.setTitle(trimToNull(course.getTitle()));
        course.setShortDescription(trimToNull(course.getShortDescription()));
        course.setDescription(trimToNull(course.getDescription()));
        course.setThumbnailUrl(trimToNull(course.getThumbnailUrl()));
        course.setLevel(trimToNull(course.getLevel()));

        if (course.getLevel() == null) {
            course.setLevel("Beginner");
        }

        if (course.getPrice() == null) {
            course.setPrice(BigDecimal.ZERO);
        }
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
