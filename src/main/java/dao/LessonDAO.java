package dao;

import model.Lesson;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {

    private static final String SELECT_ALL_BY_COURSE_ID =
            "SELECT * FROM lessons WHERE course_id = ? ORDER BY lesson_order ASC";

    private static final String SELECT_BY_ID =
            "SELECT * FROM lessons WHERE lesson_id = ?";

    private static final String INSERT_LESSON =
            "INSERT INTO lessons(course_id, title, lesson_order, content_type, content_value, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_LESSON =
            "UPDATE lessons SET course_id = ?, title = ?, lesson_order = ?, content_type = ?, content_value = ?, status = ? " +
                    "WHERE lesson_id = ?";

    private static final String DELETE_LESSON =
            "DELETE FROM lessons WHERE lesson_id = ?";

    // Show danh sách lesson của khóa học
    public List<Lesson> findByCourseId(int courseId) {
        List<Lesson> lessonList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_COURSE_ID)
        ) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lessonList.add(mapResultSetToLesson(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonList;
    }

    // Xem chi tiết 1 bài lesson
    public Lesson findById(int lessonId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, lessonId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLesson(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Thêm mới lesson
    public boolean insertLesson(Lesson lesson) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_LESSON)
        ) {
            ps.setInt(1, lesson.getCourseId());
            ps.setString(2, lesson.getTitle());
            ps.setInt(3, lesson.getLessonOrder());
            ps.setString(4, lesson.getContentType());
            ps.setString(5, lesson.getContentValue());
            ps.setInt(6, lesson.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update Lesson
    public boolean updateLesson(Lesson lesson) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_LESSON)
        ) {
            ps.setInt(1, lesson.getCourseId());
            ps.setString(2, lesson.getTitle());
            ps.setInt(3, lesson.getLessonOrder());
            ps.setString(4, lesson.getContentType());
            ps.setString(5, lesson.getContentValue());
            ps.setInt(6, lesson.getStatus());
            ps.setInt(7, lesson.getLessonId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xóa Lesson
    public boolean deleteLesson(int lessonId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_LESSON)
        ) {
            ps.setInt(1, lessonId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Lesson mapResultSetToLesson(ResultSet rs) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setLessonId(rs.getInt("lesson_id"));
        lesson.setCourseId(rs.getInt("course_id"));
        lesson.setTitle(rs.getString("title"));
        lesson.setLessonOrder(rs.getInt("lesson_order"));
        lesson.setContentType(rs.getString("content_type"));
        lesson.setContentValue(rs.getString("content_value"));
        lesson.setStatus(rs.getInt("status"));
        return lesson;
    }
}