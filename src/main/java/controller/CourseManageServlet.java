package controller;

import dao.CategoryDAO;
import dao.CourseDAO;
import model.Category;
import model.Course;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CourseManageServlet", urlPatterns = "/admin/courses")
public class CourseManageServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int ACTIVE_STATUS = 1;
    private static final int INACTIVE_STATUS = 0;

    private CourseDAO courseDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = getAdminUser(request, response);
        if (admin == null) {
            return;
        }

        String action = trimToEmpty(request.getParameter("action"));
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
                deleteCourse(request, response);
                break;
            default:
                showCourseList(request, response);
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

        String action = trimToEmpty(request.getParameter("action"));
        if ("update".equals(action)) {
            updateCourse(request, response, admin);
        } else {
            createCourse(request, response, admin);
        }
    }

    private void showCourseList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = trimToEmpty(request.getParameter("keyword"));
        Integer categoryId = parseInteger(request.getParameter("categoryId"));
        Integer status = parseInteger(request.getParameter("status"));

        List<Course> courses = findCourses(keyword, categoryId);
        courses = filterByStatus(courses, status);

        request.setAttribute("courses", courses);
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("selectedStatus", status);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/course-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "create");
        request.setAttribute("course", createEmptyCourse());
        request.setAttribute("categories", categoryDAO.findAll());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/course-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer courseId = parseInteger(request.getParameter("id"));
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=invalid-id");
            return;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=not-found");
            return;
        }

        request.setAttribute("action", "update");
        request.setAttribute("course", course);
        request.setAttribute("categories", categoryDAO.findAll());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/course-form.jsp");
        dispatcher.forward(request, response);
    }

    private void createCourse(HttpServletRequest request, HttpServletResponse response, User admin)
            throws ServletException, IOException {
        Course course = buildCourseFromRequest(request);
        course.setCreatedBy(admin.getUserId());

        String validationError = validateCourse(course);
        if (validationError != null) {
            forwardToFormWithError(request, response, "create", course, validationError);
            return;
        }

        boolean created = courseDAO.insertCourse(course);
        if (created) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=created");
        } else {
            forwardToFormWithError(
                    request,
                    response,
                    "create",
                    course,
                    "Khong the them khoa hoc. Vui long thu lai."
            );
        }
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response, User admin)
            throws ServletException, IOException {
        Integer courseId = parseInteger(request.getParameter("id"));
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=invalid-id");
            return;
        }

        Course oldCourse = courseDAO.findById(courseId);
        if (oldCourse == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=not-found");
            return;
        }

        Course course = buildCourseFromRequest(request);
        course.setCourseId(courseId);
        course.setCreatedBy(oldCourse.getCreatedBy() > 0 ? oldCourse.getCreatedBy() : admin.getUserId());

        String validationError = validateCourse(course);
        if (validationError != null) {
            forwardToFormWithError(request, response, "update", course, validationError);
            return;
        }

        boolean updated = courseDAO.updateCourse(course);
        if (updated) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=updated");
        } else {
            forwardToFormWithError(
                    request,
                    response,
                    "update",
                    course,
                    "Không thể cập nhật khóa học. Vui lòng thử lại."
            );
        }
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer courseId = parseInteger(request.getParameter("id"));
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=invalid-id");
            return;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=not-found");
            return;
        }

        boolean deleted = courseDAO.deleteCourse(courseId);
        if (deleted) {
            response.sendRedirect(request.getContextPath() + "/admin/courses?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/courses?error=delete-failed");
        }
    }

    private List<Course> findCourses(String keyword, Integer categoryId) {
        if (!keyword.isEmpty()) {
            return courseDAO.searchByTitle(keyword);
        }

        if (categoryId != null && categoryId > 0) {
            return courseDAO.findByCategory(categoryId);
        }

        return courseDAO.findAll();
    }

    private List<Course> filterByStatus(List<Course> courses, Integer status) {
        if (status == null || (status != ACTIVE_STATUS && status != INACTIVE_STATUS)) {
            return courses;
        }

        List<Course> filteredCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStatus() == status) {
                filteredCourses.add(course);
            }
        }

        return filteredCourses;
    }

    private Course buildCourseFromRequest(HttpServletRequest request) {
        Course course = new Course();
        course.setCategoryId(parseIntegerOrDefault(request.getParameter("categoryId"), 0));
        course.setTitle(trimToEmpty(request.getParameter("title")));
        course.setShortDescription(trimToEmpty(request.getParameter("shortDescription")));
        course.setDescription(trimToEmpty(request.getParameter("description")));
        course.setThumbnailUrl(trimToEmpty(request.getParameter("thumbnailUrl")));
        course.setLevel(trimToEmpty(request.getParameter("level")));
        course.setPrice(parsePrice(request.getParameter("price")));
        course.setStatus(parseStatus(request.getParameter("status")));
        return course;
    }

    private Course createEmptyCourse() {
        Course course = new Course();
        course.setPrice(BigDecimal.ZERO);
        course.setStatus(ACTIVE_STATUS);
        return course;
    }

    private String validateCourse(Course course) {
        if (course.getCategoryId() <= 0 || categoryDAO.findById(course.getCategoryId()) == null) {
            return "Vui lòng chọn danh mục hợp lệ.";
        }

        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            return "Tên kho học không được để trống.";
        }

        if (course.getTitle().length() > 150) {
            return "Tên khóa học không được quá 150 ký tự.";
        }

        if (course.getShortDescription() != null && course.getShortDescription().length() > 255) {
            return "Mô tả ngắn gọn không được quá 255 ký tự.";
        }

        if (course.getLevel() != null && course.getLevel().length() > 50) {
            return "Trình độ không được vượt quá 50 ký tự.";
        }

        if (course.getThumbnailUrl() != null && course.getThumbnailUrl().length() > 500) {
            return "Đường dẫn ảnh không được vượt quá 500 ký tự.";
        }

        if (course.getPrice() == null) {
            return "Giá khóa học không hợp lệ.";
        }

        if (course.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return "Giá khóa học không được nhỏ hơn 0.";
        }

        return null;
    }

    private void forwardToFormWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String action,
            Course course,
            String error
    ) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("action", action);
        request.setAttribute("course", course);
        request.setAttribute("categories", categoryDAO.findAll());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/course-form.jsp");
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
        if (user.getRoleId() != ADMIN_ROLE_ID) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return null;
        }

        return user;
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

    private int parseIntegerOrDefault(String value, int defaultValue) {
        Integer parsedValue = parseInteger(value);
        return parsedValue == null ? defaultValue : parsedValue;
    }

    private BigDecimal parsePrice(String value) {
        if (value == null || value.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int parseStatus(String value) {
        Integer status = parseInteger(value);
        if (status == null) {
            return ACTIVE_STATUS;
        }

        return status == INACTIVE_STATUS ? INACTIVE_STATUS : ACTIVE_STATUS;
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
