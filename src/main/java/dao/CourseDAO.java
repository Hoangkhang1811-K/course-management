package dao;

import model.Course;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private static final String SELECT_ALL =
            "SELECT * FROM courses ORDER BY course_id DESC";

    private static final String SELECT_BY_ID =
            "SELECT * FROM courses WHERE course_id = ?";

    private static final String SELECT_BY_CATEGORY =
            "SELECT * FROM courses WHERE category_id = ? ORDER BY course_id DESC";

    private static final String SEARCH_BY_TITLE =
            "SELECT * FROM courses WHERE title LIKE ? ORDER BY course_id DESC";

    private static final String INSERT_COURSE =
            "INSERT INTO courses(category_id, created_by, title, short_description, description, thumbnail_url, level, price, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_COURSE =
            "UPDATE courses SET category_id = ?, created_by = ?, title = ?, short_description = ?, description = ?, " +
                    "thumbnail_url = ?, level = ?, price = ?, status = ? WHERE course_id = ?";

    private static final String DELETE_COURSE =
            "DELETE FROM courses WHERE course_id = ?";

    // Xem danh sách khóa học
    public List<Course> findAll() {
        List<Course> courseList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                courseList.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }

    // Xem chi tiết khóa học
    public Course findById(int courseId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Filter course theo category
    public List<Course> findByCategory(int categoryId) {
        List<Course> courseList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_CATEGORY)
        ) {
            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courseList.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }

    // Search course theo title
    public List<Course> searchByTitle(String keyword) {
        List<Course> courseList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SEARCH_BY_TITLE)
        ) {
            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courseList.add(mapResultSetToCourse(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseList;
    }

    // Thêm mới khóa học
    public boolean insertCourse(Course course) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_COURSE)
        ) {
            ps.setInt(1, course.getCategoryId());
            ps.setInt(2, course.getCreatedBy());
            ps.setString(3, course.getTitle());
            ps.setString(4, course.getShortDescription());
            ps.setString(5, course.getDescription());
            ps.setString(6, course.getThumbnailUrl());
            ps.setString(7, course.getLevel());
            ps.setBigDecimal(8, course.getPrice());
            ps.setInt(9, course.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update khóa học
    public boolean updateCourse(Course course) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(UPDATE_COURSE)
        ) {
            ps.setInt(1, course.getCategoryId());
            ps.setInt(2, course.getCreatedBy());
            ps.setString(3, course.getTitle());
            ps.setString(4, course.getShortDescription());
            ps.setString(5, course.getDescription());
            ps.setString(6, course.getThumbnailUrl());
            ps.setString(7, course.getLevel());
            ps.setBigDecimal(8, course.getPrice());
            ps.setInt(9, course.getStatus());
            ps.setInt(10, course.getCourseId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xóa khóa học
    public boolean deleteCourse(int courseId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_COURSE)
        ) {
            ps.setInt(1, courseId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCategoryId(rs.getInt("category_id"));
        course.setCreatedBy(rs.getInt("created_by"));
        course.setTitle(rs.getString("title"));
        course.setShortDescription(rs.getString("short_description"));
        course.setDescription(rs.getString("description"));
        course.setThumbnailUrl(rs.getString("thumbnail_url"));
        course.setLevel(rs.getString("level"));

        BigDecimal price = rs.getBigDecimal("price");
        course.setPrice(price != null ? price : BigDecimal.ZERO);

        course.setStatus(rs.getInt("status"));
        course.setCreatedAt(rs.getTimestamp("created_at"));
        return course;
    }
}
