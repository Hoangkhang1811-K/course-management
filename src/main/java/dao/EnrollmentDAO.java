package dao;

import model.Enrollment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private static final String SELECT_ALL =
            "SELECT e.*, c.title AS course_title, u.full_name AS user_full_name " +
                    "FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "JOIN users u ON e.user_id = u.user_id " +
                    "ORDER BY e.enrollment_id DESC";

    private static final String INSERT_ENROLLMENT =
            "INSERT INTO enrollments(user_id, course_id, status) VALUES (?, ?, ?)";

    private static final String CHECK_ENROLLMENT_EXISTS =
            "SELECT 1 FROM enrollments WHERE user_id = ? AND course_id = ?";

    private static final String SELECT_BY_USER_ID =
            "SELECT e.*, c.title AS course_title, u.full_name AS user_full_name " +
                    "FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "JOIN users u ON e.user_id = u.user_id " +
                    "WHERE e.user_id = ? " +
                    "ORDER BY e.enrollment_id DESC";

    private static final String SELECT_BY_COURSE_ID =
            "SELECT e.*, c.title AS course_title, u.full_name AS user_full_name " +
                    "FROM enrollments e " +
                    "JOIN courses c ON e.course_id = c.course_id " +
                    "JOIN users u ON e.user_id = u.user_id " +
                    "WHERE e.course_id = ? " +
                    "ORDER BY e.enrollment_id DESC";

    private static final String SELECT_BY_ID =
            "SELECT * FROM enrollments WHERE enrollment_id = ?";

    private static final String UPDATE_STATUS =
            "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";

    private static final String DELETE_ENROLLMENT =
            "DELETE FROM enrollments WHERE enrollment_id = ?";

    public List<Enrollment> findAll() {
        List<Enrollment> enrollmentList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                enrollmentList.add(mapResultSetToEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollmentList;
    }

    // Kết quả đăng ký khóa học
    public boolean insertEnrollment(Enrollment enrollment) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_ENROLLMENT)
        ) {
            ps.setInt(1, enrollment.getUserId());
            ps.setInt(2, enrollment.getCourseId());
            ps.setString(3, enrollment.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check xem học viên đã đăng ký khóa học chưa
    public boolean existsByUserAndCourse(int userId, int courseId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(CHECK_ENROLLMENT_EXISTS)
        ) {
            ps.setInt(1, userId);
            ps.setInt(2, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // xem học viên đã đăng ký khóa học nào
    public Enrollment findById(int enrollmentId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, enrollmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Xem các khóa học của học viên
    public List<Enrollment> findByUserId(int userId) {
        List<Enrollment> enrollmentList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER_ID)
        ) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollmentList.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollmentList;
    }

    // xem danh sách học viên theo khóa học
    public List<Enrollment> findByCourseId(int courseId) {
        List<Enrollment> enrollmentList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_COURSE_ID)
        ) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollmentList.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollmentList;
    }

    // update trạng thái của khóa học
    public boolean updateStatus(int enrollmentId, String status) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)
        ) {
            ps.setString(1, status);
            ps.setInt(2, enrollmentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // xóa khóa học
    public boolean deleteEnrollment(int enrollmentId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_ENROLLMENT)
        ) {
            ps.setInt(1, enrollmentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
        enrollment.setUserId(rs.getInt("user_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setEnrolledAt(rs.getTimestamp("enrolled_at"));
        enrollment.setStatus(rs.getString("status"));

        try {
            enrollment.setCourseTitle(rs.getString("course_title"));
        } catch (SQLException e) {
            // Query khong join courses thi khong co cot nay.
        }

        try {
            enrollment.setUserFullName(rs.getString("user_full_name"));
        } catch (SQLException e) {
            // Query khong join users thi khong co cot nay.
        }

        return enrollment;
    }
}
