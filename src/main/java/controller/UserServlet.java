package controller;

import model.User;
import service.UserService;
import util.ValidateUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "UserServlet", urlPatterns = "/admin/users")
public class UserServlet extends HttpServlet {
    private static final String LIST_JSP = "/views/admin/user-list.jsp";
    private static final String FORM_JSP = "/views/admin/user-form.jsp";

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = getAdminUser(request, response);
        if (admin == null) {
            return;
        }

        String action = ValidateUtil.trimToEmpty(request.getParameter("action"));
        if (action.isEmpty()) {
            action = "list";
        }

        switch (action) {
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response, admin);
                break;
            case "lock":
                updateUserStatus(request, response, admin, UserService.INACTIVE_STATUS);
                break;
            case "unlock":
                updateUserStatus(request, response, admin, UserService.ACTIVE_STATUS);
                break;
            default:
                showUserList(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = getAdminUser(request, response);
        if (admin == null) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = ValidateUtil.trimToEmpty(request.getParameter("action"));
        if ("update".equals(action)) {
            updateUser(request, response, admin);
        } else {
            createUser(request, response);
        }
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = ValidateUtil.trimToEmpty(request.getParameter("keyword"));
        Integer roleId = ValidateUtil.parseInteger(request.getParameter("roleId"));
        Integer status = ValidateUtil.parseInteger(request.getParameter("status"));

        List<User> users = userService.filterUsers(keyword, roleId, status);

        consumeFlashMessages(request);
        request.setAttribute("users", users);
        request.setAttribute("totalUsers", users.size());
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedRoleId", roleId);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("adminRoleId", UserService.ADMIN_ROLE_ID);
        request.setAttribute("studentRoleId", UserService.STUDENT_ROLE_ID);

        RequestDispatcher dispatcher = request.getRequestDispatcher(LIST_JSP);
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "create");
        request.setAttribute("user", createEmptyUser());
        setRoleAttributes(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = ValidateUtil.parseInteger(request.getParameter("id"));
        if (userId == null || userId <= 0) {
            setFlashError(request, "Ma nguoi dung khong hop le.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            setFlashError(request, "Nguoi dung khong ton tai.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        request.setAttribute("action", "update");
        request.setAttribute("user", user);
        setRoleAttributes(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(FORM_JSP);
        dispatcher.forward(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = buildUserFromRequest(request);
        String password = ValidateUtil.trimToEmpty(request.getParameter("password"));

        String validationError = userService.validateCreateUser(
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                password,
                user.getRoleId(),
                user.getStatus()
        );

        if (validationError != null) {
            forwardToFormWithError(request, response, "create", user, validationError);
            return;
        }

        boolean created = userService.createUser(
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                password,
                user.getRoleId(),
                user.getStatus()
        );

        if (created) {
            setFlashSuccess(request, "Them nguoi dung thanh cong.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else {
            forwardToFormWithError(request, response, "create", user, "Khong the them nguoi dung. Vui long thu lai.");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response, User admin)
            throws ServletException, IOException {
        Integer userId = ValidateUtil.parseInteger(request.getParameter("id"));
        if (userId == null || userId <= 0) {
            setFlashError(request, "Ma nguoi dung khong hop le.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        User user = buildUserFromRequest(request);
        user.setUserId(userId);
        String password = ValidateUtil.trimToEmpty(request.getParameter("password"));

        String validationError = userService.validateUpdateUser(
                userId,
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                password,
                user.getRoleId(),
                user.getStatus()
        );

        if (validationError != null) {
            forwardToFormWithError(request, response, "update", user, validationError);
            return;
        }

        if (admin.getUserId() == userId && user.getStatus() == UserService.INACTIVE_STATUS) {
            forwardToFormWithError(request, response, "update", user, "Khong the khoa tai khoan dang dang nhap.");
            return;
        }

        boolean updated = userService.updateUser(
                userId,
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                password,
                user.getRoleId(),
                user.getStatus()
        );

        if (updated) {
            refreshSessionUserIfNeeded(request, admin, userId);
            setFlashSuccess(request, "Cap nhat nguoi dung thanh cong.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else {
            forwardToFormWithError(request, response, "update", user, "Khong the cap nhat nguoi dung. Vui long thu lai.");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response, User admin)
            throws IOException {
        Integer userId = ValidateUtil.parseInteger(request.getParameter("id"));
        if (userId == null || userId <= 0) {
            setFlashError(request, "Ma nguoi dung khong hop le.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        if (admin.getUserId() == userId) {
            setFlashError(request, "Khong the xoa tai khoan dang dang nhap.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            setFlashSuccess(request, "Xoa nguoi dung thanh cong.");
        } else {
            setFlashError(request, "Khong the xoa nguoi dung. Vui long thu lai.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private void updateUserStatus(HttpServletRequest request, HttpServletResponse response, User admin, int status)
            throws IOException {
        Integer userId = ValidateUtil.parseInteger(request.getParameter("id"));
        if (userId == null || userId <= 0) {
            setFlashError(request, "Ma nguoi dung khong hop le.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        if (admin.getUserId() == userId && status == UserService.INACTIVE_STATUS) {
            setFlashError(request, "Khong the khoa tai khoan dang dang nhap.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }

        boolean updated = userService.updateStatus(userId, status);
        if (updated) {
            setFlashSuccess(request, status == UserService.ACTIVE_STATUS
                    ? "Mo khoa nguoi dung thanh cong."
                    : "Khoa nguoi dung thanh cong.");
        } else {
            setFlashError(request, "Khong the cap nhat trang thai nguoi dung.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    private User buildUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setRoleId(parseRoleId(request.getParameter("roleId")));
        user.setFullName(ValidateUtil.trimToEmpty(request.getParameter("fullName")));
        user.setEmail(ValidateUtil.trimToEmpty(request.getParameter("email")).toLowerCase(Locale.ROOT));
        user.setPhone(ValidateUtil.trimToEmpty(request.getParameter("phone")));
        user.setStatus(ValidateUtil.parseStatus(request.getParameter("status")));
        return user;
    }

    private User createEmptyUser() {
        User user = new User();
        user.setRoleId(UserService.STUDENT_ROLE_ID);
        user.setStatus(UserService.ACTIVE_STATUS);
        return user;
    }

    private void forwardToFormWithError(HttpServletRequest request, HttpServletResponse response, String action, User user, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("action", action);
        request.setAttribute("user", user);
        setRoleAttributes(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher(FORM_JSP);
        dispatcher.forward(request, response);
    }

    private User getAdminUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        Object userObject = session.getAttribute("loggedInUser");
        if (!(userObject instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        User user = (User) userObject;
        if (user.getRoleId() != UserService.ADMIN_ROLE_ID) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return null;
        }

        return user;
    }

    private int parseRoleId(String value) {
        Integer roleId = ValidateUtil.parseInteger(value);
        if (roleId == null || !userService.isValidRole(roleId)) {
            return UserService.STUDENT_ROLE_ID;
        }

        return roleId;
    }

    private void setRoleAttributes(HttpServletRequest request) {
        request.setAttribute("adminRoleId", UserService.ADMIN_ROLE_ID);
        request.setAttribute("studentRoleId", UserService.STUDENT_ROLE_ID);
    }

    private void refreshSessionUserIfNeeded(HttpServletRequest request, User admin, int updatedUserId) {
        if (admin.getUserId() != updatedUserId) {
            return;
        }

        User updatedAdmin = userService.getUserById(updatedUserId);
        if (updatedAdmin != null) {
            request.getSession().setAttribute("loggedInUser", updatedAdmin);
        }
    }

    private void setFlashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("error", message);
    }

    private void setFlashSuccess(HttpServletRequest request, String message) {
        request.getSession().setAttribute("successMsg", message);
    }

    private void consumeFlashMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        Object error = session.getAttribute("error");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        Object successMsg = session.getAttribute("successMsg");
        if (successMsg != null) {
            request.setAttribute("successMsg", successMsg);
            session.removeAttribute("successMsg");
        }
    }
}
