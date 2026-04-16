package service;

import dao.EnrollmentDAO;
import model.Enrollment;

import java.util.Collections;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    private static final String STATUS_ENROLLED = "ENROLLED";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_CANCELLED = "COMPLETED";

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    // Lấy danh sách enrollment
    public List<Enrollment> findAll() {
        return enrollmentDAO.findAll();
    }

    // Lấy enrollment theo id
    public Enrollment findById(int enrollmentId) {
        if (enrollmentId <=0) {
            return null;
        }
        return enrollmentDAO.findById(enrollmentId);
    }

    // Lấy danh sách khoá học mà user đã đăng ký
    public List<Enrollment> findByUserId(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        return enrollmentDAO.findByCourseId(userId);
    }

    // Lấy danh sách học viên theo khóa học
    public List<Enrollment> findByCourseId(int courseId) {
        if (courseId <= 0) {
            return Collections.emptyList();
        }
        return enrollmentDAO.findByCourseId(courseId);
    }

    // Kiểm tra user đã đăng ký course chưa
    public boolean existsByUserAndCourse(int userId, int courseId) {
        if (userId <= 0 || courseId <= 0) {
            return false;
        }
        return enrollmentDAO.existsByUserAndCourse(userId, courseId);
    }

    // Đăng ký khóa học bằng object Enrollment
    public boolean insertEnrollment(Enrollment enrollment) {
        if (!isValidEnrollment(enrollment)) {
            return false;
        }

        enrollment.setStatus(normalizeStatus(enrollment.getStatus()));

        if (enrollmentDAO.existsByUserAndCourse(enrollment.getUserId(), enrollment.getCourseId())) {
            return false;
        }

        return enrollmentDAO.insertEnrollment(enrollment);
    }

    // Đăng ký khóa học bằng tham số rời
    public boolean insertEnrollment(int userId, int courseId, String status) {
        if (!isValidUserId(userId) || !isValidCourseId(courseId) || !isValidStatus(status)) {
            return false;
        }

        if (enrollmentDAO.existsByUserAndCourse(userId, courseId)) {
            return false;
        }

        Enrollment enrollment = new Enrollment(userId, courseId, normalizeStatus(status));
        return enrollmentDAO.insertEnrollment(enrollment);
    }

    // Cập nhật trạng thái enrollment
    public boolean updateStatus(int enrollmentId, String status) {
        if (enrollmentId <= 0 || !isValidStatus(status)) {
            return false;
        }

        Enrollment existingEnrollment = enrollmentDAO.findById(enrollmentId);
        if (existingEnrollment == null) {
            return false;
        }

        return enrollmentDAO.updateStatus(enrollmentId, normalizeStatus(status));
    }

    // Xóa enrollment
    public boolean deleteEnrollment(int enrollmentId) {
        if (enrollmentId <= 0) {
            return false;
        }

        Enrollment existingEnrollment = enrollmentDAO.findById(enrollmentId);
        if (existingEnrollment == null) {
            return false;
        }

        return enrollmentDAO.deleteEnrollment(enrollmentId);
    }

    private boolean isValidEnrollment(Enrollment enrollment) {
        return enrollment != null
                && isValidUserId(enrollment.getUserId())
                && isValidCourseId(enrollment.getCourseId())
                && isValidStatus(enrollment.getStatus());
    }

    private boolean isValidUserId(int userId) {
        return userId > 0;
    }

    private boolean isValidCourseId(int courseId) {
        return courseId > 0;
    }

    private boolean isValidStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        String normalizedStatus = normalizeStatus(status);
        return STATUS_ENROLLED.equals(normalizedStatus)
                || STATUS_COMPLETED.equals(normalizedStatus)
                || STATUS_CANCELLED.equals(normalizedStatus);
    }

    private String normalizeStatus(String status) {
        return status.trim().toUpperCase();
    }
}
