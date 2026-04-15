package controller;

import model.User;
import service.AuthService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int STUDENT_ROLE_ID = 2;
    private static final int ACTIVE_STATUS = 1;
    private static final int MIN_PASSWORD_LENGTH = 6;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9+\\-\\s]{9,15}$"
    );

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedInUser = getLoggedInUser(request);
        if (loggedInUser != null) {
            redirectAfterLogin(request, response, loggedInUser);
            return;
        }

        forwardToRegister(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String fullName = trimToEmpty(request.getParameter("fullName"));
        String email = trimToEmpty(request.getParameter("email")).toLowerCase();
        String phone = trimToEmpty(request.getParameter("phone"));
        String password = trimToEmpty(request.getParameter("password"));
        String confirmPassword = trimToEmpty(request.getParameter("confirmPassword"));

        keepFormValue(request, fullName, email, phone);

        String validationError = validateRegisterForm(fullName, email, phone, password, confirmPassword);
        if (validationError != null) {
            request.setAttribute("error", validationError);
            forwardToRegister(request, response);
            return;
        }

        if (authService.isEmailExist(email)) {
            request.setAttribute("error", "Email này đã được sử dụng. Vui lòng dùng email khác.");
            forwardToRegister(request, response);
            return;
        }

        User user = new User(STUDENT_ROLE_ID, fullName, email, password, normalizePhone(phone), ACTIVE_STATUS);
        boolean registered = authService.register(user);

        if (registered) {
            HttpSession session = request.getSession();
            session.setAttribute("successMsg", "Đăng ký tài khoản thành công. Vui lòng đăng nhập bằng email của bạn.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("error", "Không thể đăng ký tài khoản. Vui lòng kiểm tra kết nối cơ sở dữ liệu và thử lại.");
        forwardToRegister(request, response);
    }

    private String validateRegisterForm(
            String fullName,
            String email,
            String phone,
            String password,
            String confirmPassword
    ) {
        if (fullName.isEmpty()) {
            return "Họ và tên không được để trống.";
        }

        if (fullName.length() > 100) {
            return "Họ và tên không được vượt quá 100 ký tự.";
        }

        if (email.isEmpty()) {
            return "Email không được để trống.";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Email không đúng định dạng.";
        }

        if (email.length() > 100) {
            return "Email không được vượt quá 100 ký tự.";
        }

        if (!phone.isEmpty() && !PHONE_PATTERN.matcher(phone).matches()) {
            return "Số điện thoại không hợp lệ. Vui lòng nhập từ 9 đến 15 ký tự số.";
        }

        if (password.isEmpty()) {
            return "Mật khẩu không được để trống.";
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất 6 ký tự.";
        }

        if (password.length() > 100) {
            return "Mật khẩu không được vượt quá 100 ký tự.";
        }

        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp.";
        }

        return null;
    }

    private void keepFormValue(HttpServletRequest request, String fullName, String email, String phone) {
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
    }

    private void forwardToRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/register.jsp");
        dispatcher.forward(request, response);
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object userObject = session.getAttribute("loggedInUser");
        if (userObject instanceof User) {
            return (User) userObject;
        }

        return null;
    }

    private void redirectAfterLogin(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        if (user.getRoleId() == ADMIN_ROLE_ID) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    private String normalizePhone(String phone) {
        return phone.isEmpty() ? null : phone;
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
