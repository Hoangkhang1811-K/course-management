package controller;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.UserDAO;
import model.Course;
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
import java.util.Locale;

@WebServlet(name = "EnrollmentManageServlet", urlPatterns = "/admin/enrollments")
public class EnrollmentManageServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int STUDENT_ROLE_ID = 2;

    private static final String STATUS_ENROLLED = "ENROLLED";
    private static final String STATUS_CANCELLED = "CANCELLED";
    private static final String STATUS_COMPLETED = "COMPLETED";

    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = trimToEmpty(request.getParameter("action"));
        if ("delete".equals(action)) {
            deleteEnrollment(request, response);
            return;
        }

        showEnrollmentList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = trimToEmpty(request.getParameter("action"));
        switch (action) {
            case "create":
                createEnrollment(request, response);
                break;
            case "updateStatus":
                updateEnrollmentStatus(request, response);
                break;
            case "delete":
                deleteEnrollment(request, response);
                break;
            default:
                setFlashError(request, "Thao tác không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/admin/enrollments");
                break;
        }
    }

    private void showEnrollmentList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer courseId = parseInteger(request.getParameter("courseId"));
        Integer userId = parseInteger(request.getParameter("userId"));
        String status = normalizeStatus(request.getParameter("status"));
        String keyword = trimToEmpty(request.getParameter("keyword"));

        List<Enrollment> enrollments = findEnrollments(courseId, userId);
        enrollments = filterByStatus(enrollments, status);
        enrollments = filterByKeyword(enrollments, keyword);

        consumeFlashMessages(request);
        request.setAttribute("enrollments", enrollments);
        request.setAttribute("courses", courseDAO.findAll());
        request.setAttribute("students", userDAO.findAllStudents());
        request.setAttribute("statuses", getStatuses());
        request.setAttribute("selectedCourseId", courseId);
        request.setAttribute("selectedUserId", userId);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("keyword", keyword);
        request.setAttribute("totalEnrollments", enrollments.size());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/enrollment-list.jsp");
        dispatcher.forward(request, response);
    }

    private void createEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer userId = parseInteger(request.getParameter("userId"));
        Integer courseId = parseInteger(request.getParameter("courseId"));
        String status = normalizeStatus(request.getParameter("status"));

        if (userId == null || userId <= 0) {
            setFlashError(request, "Vui lòng chọn học viên hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        if (courseId == null || courseId <= 0) {
            setFlashError(request, "Vui lòng chọn khóa học hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        User student = userDAO.findByID(userId);
        if (student == null || student.getRoleId() != STUDENT_ROLE_ID) {
            setFlashError(request, "Học viên không tồn tại hoặc không đúng vai trò học viên.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            setFlashError(request, "Khóa học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        if (status == null) {
            status = STATUS_ENROLLED;
        }

        if (enrollmentDAO.existsByUserAndCourse(userId, courseId)) {
            setFlashError(request, "Học viên đã đăng ký khóa học này.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        boolean created = enrollmentDAO.insertEnrollment(new Enrollment(userId, courseId, status));
        if (created) {
            setFlashSuccess(request, "Thêm đăng ký khóa học thành công.");
        } else {
            setFlashError(request, "Không thể thêm đăng ký khóa học. Vui lòng thử lại.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/enrollments");
    }

    private void updateEnrollmentStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer enrollmentId = parseInteger(request.getParameter("id"));
        String status = normalizeStatus(request.getParameter("status"));

        if (enrollmentId == null || enrollmentId <= 0) {
            setFlashError(request, "Mã đăng ký không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        if (status == null) {
            setFlashError(request, "Trạng thái đăng ký không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        if (enrollment == null) {
            setFlashError(request, "Đăng ký khóa học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        boolean updated = enrollmentDAO.updateStatus(enrollmentId, status);
        if (updated) {
            setFlashSuccess(request, "Cập nhật trạng thái đăng ký thành công.");
        } else {
            setFlashError(request, "Không thể cập nhật trạng thái đăng ký. Vui lòng thử lại.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/enrollments");
    }

    private void deleteEnrollment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer enrollmentId = parseInteger(request.getParameter("id"));

        if (enrollmentId == null || enrollmentId <= 0) {
            setFlashError(request, "Mã đăng ký không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        if (enrollment == null) {
            setFlashError(request, "Đăng ký khóa học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/enrollments");
            return;
        }

        boolean deleted = enrollmentDAO.deleteEnrollment(enrollmentId);
        if (deleted) {
            setFlashSuccess(request, "Xóa đăng ký khóa học thành công.");
        } else {
            setFlashError(request, "Không thể xóa đăng ký khóa học. Vui lòng thử lại.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/enrollments");
    }

    private List<Enrollment> findEnrollments(Integer courseId, Integer userId) {
        if (courseId != null && courseId > 0) {
            return enrollmentDAO.findByCourseId(courseId);
        }

        if (userId != null && userId > 0) {
            return enrollmentDAO.findByUserId(userId);
        }

        return enrollmentDAO.findAll();
    }

    private List<Enrollment> filterByStatus(List<Enrollment> enrollments, String status) {
        if (status == null) {
            return enrollments;
        }

        List<Enrollment> filteredEnrollments = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            if (status.equals(enrollment.getStatus())) {
                filteredEnrollments.add(enrollment);
            }
        }

        return filteredEnrollments;
    }

    private List<Enrollment> filterByKeyword(List<Enrollment> enrollments, String keyword) {
        if (keyword.isEmpty()) {
            return enrollments;
        }

        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Enrollment> filteredEnrollments = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            String courseTitle = trimToEmpty(enrollment.getCourseTitle()).toLowerCase(Locale.ROOT);
            String userFullName = trimToEmpty(enrollment.getUserFullName()).toLowerCase(Locale.ROOT);

            if (courseTitle.contains(lowerKeyword) || userFullName.contains(lowerKeyword)) {
                filteredEnrollments.add(enrollment);
            }
        }

        return filteredEnrollments;
    }

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Object userObject = session.getAttribute("loggedInUser");
        if (!(userObject instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        User user = (User) userObject;
        if (user.getRoleId() != ADMIN_ROLE_ID) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return false;
        }

        return true;
    }

    private List<String> getStatuses() {
        return Arrays.asList(STATUS_ENROLLED, STATUS_CANCELLED, STATUS_COMPLETED);
    }

    private String normalizeStatus(String status) {
        String normalizedStatus = trimToEmpty(status).toUpperCase(Locale.ROOT);
        if (getStatuses().contains(normalizedStatus)) {
            return normalizedStatus;
        }

        return null;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
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

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
