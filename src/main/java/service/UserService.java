package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;
import util.ValidateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserService {
    public static final int ADMIN_ROLE_ID = 1;
    public static final int STUDENT_ROLE_ID = 2;
    public static final int ACTIVE_STATUS = 1;
    public static final int INACTIVE_STATUS = 0;

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getAllStudents() {
        return userDAO.findAllStudents();
    }

    public User getUserById(int userId) {
        if (userId <= 0) {
            return null;
        }

        return userDAO.findByID(userId);
    }

    public List<User> filterUsers(String keyword, Integer roleId, Integer status) {
        List<User> users = userDAO.findAll();
        List<User> filteredUsers = new ArrayList<>();

        String normalizedKeyword = ValidateUtil.trimToEmpty(keyword).toLowerCase(Locale.ROOT);

        for (User user : users) {
            if (roleId != null && roleId > 0 && user.getRoleId() != roleId) {
                continue;
            }

            if (status != null && isValidStatus(status) && user.getStatus() != status) {
                continue;
            }

            if (!normalizedKeyword.isEmpty() && !containsKeyword(user, normalizedKeyword)) {
                continue;
            }

            filteredUsers.add(user);
        }

        return filteredUsers;
    }

    public String validateCreateUser(
            String fullName,
            String email,
            String phone,
            String password,
            int roleId,
            int status
    ) {
        String commonError = validateCommonUserData(fullName, email, phone, roleId, status);
        if (commonError != null) {
            return commonError;
        }

        if (ValidateUtil.isBlank(password)) {
            return "Mat khau khong duoc de trong.";
        }

        if (password.length() < ValidateUtil.MIN_PASSWORD_LENGTH) {
            return "Mat khau phai co it nhat 6 ky tu.";
        }

        if (password.length() > 100) {
            return "Mat khau khong duoc vuot qua 100 ky tu.";
        }

        if (isEmailExist(email)) {
            return "Email nay da duoc su dung. Vui long dung email khac.";
        }

        return null;
    }

    public String validateUpdateUser(
            int userId,
            String fullName,
            String email,
            String phone,
            String password,
            int roleId,
            int status
    ) {
        if (userId <= 0) {
            return "Ma nguoi dung khong hop le.";
        }

        User existingUser = userDAO.findByID(userId);
        if (existingUser == null) {
            return "Nguoi dung khong ton tai.";
        }

        String commonError = validateCommonUserData(fullName, email, phone, roleId, status);
        if (commonError != null) {
            return commonError;
        }

        if (!ValidateUtil.isBlank(password)) {
            if (password.length() < ValidateUtil.MIN_PASSWORD_LENGTH) {
                return "Mat khau phai co it nhat 6 ky tu.";
            }

            if (password.length() > 100) {
                return "Mat khau khong duoc vuot qua 100 ky tu.";
            }
        }

        User emailOwner = userDAO.findByEmail(normalizeEmail(email));
        if (emailOwner != null && emailOwner.getUserId() != userId) {
            return "Email nay da duoc su dung. Vui long dung email khac.";
        }

        return null;
    }

    public boolean createUser(String fullName, String email, String phone, String password, int roleId, int status) {
        String validationError = validateCreateUser(fullName, email, phone, password, roleId, status);
        if (validationError != null) {
            return false;
        }

        User user = new User(
                roleId,
                ValidateUtil.trimToEmpty(fullName),
                normalizeEmail(email),
                PasswordUtil.hashPassword(password),
                ValidateUtil.normalizePhone(phone),
                status
        );

        return userDAO.insertUser(user);
    }

    public boolean updateUser(int userId, String fullName, String email, String phone, String password, int roleId, int status) {
        String validationError = validateUpdateUser(userId, fullName, email, phone, password, roleId, status);
        if (validationError != null) {
            return false;
        }

        User user = userDAO.findByID(userId);
        if (user == null) {
            return false;
        }

        user.setRoleId(roleId);
        user.setFullName(ValidateUtil.trimToEmpty(fullName));
        user.setEmail(normalizeEmail(email));
        user.setPhone(ValidateUtil.normalizePhone(phone));
        user.setStatus(status);

        if (!ValidateUtil.isBlank(password)) {
            user.setPasswordHash(PasswordUtil.hashPassword(password));
        }

        return userDAO.updateUser(user);
    }

    public boolean updateStatus(int userId, int status) {
        if (userId <= 0 || !isValidStatus(status) || userDAO.findByID(userId) == null) {
            return false;
        }

        return userDAO.updateStatus(userId, status);
    }

    public boolean deleteUser(int userId) {
        if (userId <= 0 || userDAO.findByID(userId) == null) {
            return false;
        }

        return userDAO.deleteUser(userId);
    }

    public boolean isEmailExist(String email) {
        return userDAO.findByEmail(normalizeEmail(email)) != null;
    }

    public boolean isValidRole(int roleId) {
        return roleId == ADMIN_ROLE_ID || roleId == STUDENT_ROLE_ID;
    }

    public boolean isValidStatus(int status) {
        return status == ACTIVE_STATUS || status == INACTIVE_STATUS;
    }

    private String validateCommonUserData(String fullName, String email, String phone, int roleId, int status) {
        if (ValidateUtil.isBlank(fullName)) {
            return "Ho va ten khong duoc de trong.";
        }

        if (!ValidateUtil.isValidLength(ValidateUtil.trimToEmpty(fullName), 100)) {
            return "Ho va ten khong duoc vuot qua 100 ky tu.";
        }

        if (ValidateUtil.isBlank(email)) {
            return "Email khong duoc de trong.";
        }

        if (!ValidateUtil.isValidEmail(email)) {
            return "Email khong dung dinh dang.";
        }

        if (!ValidateUtil.isValidLength(ValidateUtil.trimToEmpty(email), 100)) {
            return "Email khong duoc vuot qua 100 ky tu.";
        }

        if (!ValidateUtil.isValidPhone(phone)) {
            return "So dien thoai khong hop le. Vui long nhap dung 10 chu so.";
        }

        if (!isValidRole(roleId)) {
            return "Vai tro nguoi dung khong hop le.";
        }

        if (!isValidStatus(status)) {
            return "Trang thai nguoi dung khong hop le.";
        }

        return null;
    }

    private boolean containsKeyword(User user, String keyword) {
        String fullName = ValidateUtil.trimToEmpty(user.getFullName()).toLowerCase(Locale.ROOT);
        String email = ValidateUtil.trimToEmpty(user.getEmail()).toLowerCase(Locale.ROOT);
        String phone = ValidateUtil.trimToEmpty(user.getPhone()).toLowerCase(Locale.ROOT);

        return fullName.contains(keyword) || email.contains(keyword) || phone.contains(keyword);
    }

    private String normalizeEmail(String email) {
        return ValidateUtil.trimToEmpty(email).toLowerCase(Locale.ROOT);
    }
}
