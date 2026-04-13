package controller;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import model.Course;
import model.Enrollment;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EnrollServlet", urlPatterns = "/enroll")
public class EnrollServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int STUDENT_ROLE_ID = 2;
    private static final int ACTIVE_STATUS = 1;
    private static final String ENROLLMENT_STATUS = "ENROLLED";

    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        handleEnroll(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        handleEnroll(request, response);
    }

    private void handleEnroll(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer courseId = getCourseId(request);
        if (courseId == null || courseId <= 0) {
            setFlashError(request, "Mã khóa học không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }

        User loggedInUser = getLoggedInUser(request);
        if (loggedInUser == null) {
            setFlashError(request, "Vui lòng đăng nhập để đăng ký khóa học.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (loggedInUser.getRoleId() == ADMIN_ROLE_ID) {
            setFlashError(request, "Tài khoản quản trị không thể đăng ký khóa học.");
            response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
            return;
        }

        if (loggedInUser.getRoleId() != STUDENT_ROLE_ID) {
            setFlashError(request, "Chỉ học viên mới có thể đăng ký khóa học.");
            response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
            return;
        }

        if (loggedInUser.getStatus() != ACTIVE_STATUS) {
            setFlashError(request, "Tài khoản của bạn đang bị khóa, không thể đăng ký khóa học.");
            response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
            return;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            setFlashError(request, "Khóa học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/courses");
            return;
        }

        if (course.getStatus() != ACTIVE_STATUS) {
            setFlashError(request, "Khóa học hiện không mở đăng ký.");
            response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
            return;
        }

        if (enrollmentDAO.existsByUserAndCourse(loggedInUser.getUserId(), courseId)) {
            setFlashError(request, "Bạn đã đăng ký khóa học này rồi.");
            response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
            return;
        }

        Enrollment enrollment = new Enrollment(loggedInUser.getUserId(), courseId, ENROLLMENT_STATUS);
        boolean enrolled = enrollmentDAO.insertEnrollment(enrollment);

        if (enrolled) {
            setFlashSuccess(request, "Đăng ký khóa học thành công.");
        } else {
            setFlashError(request, "Không thể đăng ký khóa học. Vui lòng thử lại.");
        }

        response.sendRedirect(request.getContextPath() + "/course-detail?id=" + courseId);
    }

    private Integer getCourseId(HttpServletRequest request) {
        String value = request.getParameter("courseId");
        if (value == null || value.trim().isEmpty()) {
            value = request.getParameter("id");
        }

        return parseInteger(value);
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
}
