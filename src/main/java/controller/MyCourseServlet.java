package controller;

import dao.EnrollmentDAO;
import model.Enrollment;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "MyCourseServlet", urlPatterns = "/my-courses")
public class MyCourseServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int STUDENT_ROLE_ID = 2;
    private static final int ACTIVE_STATUS = 1;

    private static final String STATUS_ENROLLED = "ENROLLED";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final List<String> VALID_STATUSES = Arrays.asList(
            STATUS_ENROLLED,
            STATUS_COMPLETED,
            STATUS_CANCELLED
    );

    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        User loggedInUser = getLoggedInUser(request);
        if (loggedInUser == null) {
            setFlashError(request, "Vui lòng đăng nhập để xem khóa học của bạn.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (loggedInUser.getRoleId() == ADMIN_ROLE_ID) {
            setFlashError(request, "Tài khoản quản trị không có danh sách khóa học cá nhân.");
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        if (loggedInUser.getRoleId() != STUDENT_ROLE_ID) {
            setFlashError(request, "Chỉ học viên mới có thể xem khóa học của tôi.");
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }

        if (loggedInUser.getStatus() != ACTIVE_STATUS) {
            setFlashError(request, "Tài khoản của bạn đang bị khóa. Vui lòng liên hệ quản trị viên.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String selectedStatus = normalizeStatus(request.getParameter("status"));
        List<Enrollment> enrollments = enrollmentDAO.findByUserId(loggedInUser.getUserId());
        enrollments = filterByStatus(enrollments, selectedStatus);

        if (enrollments.isEmpty()) {
            if (selectedStatus.isEmpty()) {
                request.setAttribute("message", "Bạn chưa đăng ký khóa học nào.");
            } else {
                request.setAttribute("message", "Không tìm thấy khóa học phù hợp với trạng thái đã chọn.");
            }
        }

        request.setAttribute("enrollments", enrollments);
        request.setAttribute("statuses", VALID_STATUSES);
        request.setAttribute("selectedStatus", selectedStatus);
        request.setAttribute("totalEnrollments", enrollments.size());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/client/my-courses.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private List<Enrollment> filterByStatus(List<Enrollment> enrollments, String selectedStatus) {
        if (selectedStatus.isEmpty()) {
            return enrollments;
        }

        List<Enrollment> filteredEnrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (selectedStatus.equals(enrollment.getStatus())) {
                filteredEnrollments.add(enrollment);
            }
        }

        return filteredEnrollments;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "";
        }

        String normalizedStatus = status.trim().toUpperCase();
        return VALID_STATUSES.contains(normalizedStatus) ? normalizedStatus : "";
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

    private void setFlashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("error", message);
    }
}
