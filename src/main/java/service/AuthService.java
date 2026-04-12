package service;

import dao.UserDAO;
import model.User;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    // Hàm đăng nhập (email là username)
    public User login(String email, String password) {
        User user = userDAO.findByEmail(email);

        if (user == null) {
            return null;
        }

        if (user.getStatus() == 0) {
            return null;
        }

        if (!user.getPasswordHash().equals(password)) {
            return null;
        }

        return user;
    }

    // Hàm đăng ký tài khoản, có check existing user
    public boolean register(User user) {
        User existingUser = userDAO.findByEmail(user.getEmail());

        if (existingUser != null) {
            return false;
        }
        return userDAO.insertUser(user);
    }

    // Hàm check email existing
    public boolean isEmailExist(String email) {
        User user = userDAO.findByEmail(email);

        if (user != null) {
            return true;
        } else  {
            return false;
        }
    }

    public User findByEmail(String email) {
        User user = userDAO.findByEmail(email);
        return user;
    }

    public User findById(int userId) {
        return userDAO.findByID(userId);
    }
}
