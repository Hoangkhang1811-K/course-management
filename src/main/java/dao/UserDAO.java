package dao;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String SELECT_BY_EMAIL = "select * from users where email = ?";
    private static final String SELECT_BY_ID = "select * from users where user_id = ?";
    private static final String INSERT_USER = "insert into users (role_id, full_name, email, password_hash, phone, status) + values (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_STUDENT = "select * from users where role_id = 2 ORDER BY user_id DESC";
    private static final String SELECT_ALL_USERS = "select * from users ORDER BY user_id DESC";

    public User findByEmail(String email) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByID(int userId) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)
        ) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertUser(User user) {
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(INSERT_USER)
        ) {
            ps.setInt(1, user.getRoleId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getPhone());
            ps.setInt(6, user.getStatus());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> findAllStudents() {
        List<User> studentList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ALL_STUDENT);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                studentList.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setRoleId(rs.getInt("role_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setPhone(rs.getString("phone"));
        user.setStatus(rs.getInt("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}
