package controller;

import dao.CourseDAO;
import dao.LessonDAO;
import model.Course;
import model.Lesson;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "LessonServlet", urlPatterns = "/admin/lessons")
public class LessonServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int ACTIVE_STATUS = 1;
    private static final int INACTIVE_STATUS = 0;

    private static final String CONTENT_TYPE_TEXT = "TEXT";
    private static final String CONTENT_TYPE_VIDEO = "VIDEO";
    private static final List<String> CONTENT_TYPES = Arrays.asList(CONTENT_TYPE_TEXT, CONTENT_TYPE_VIDEO);

    private CourseDAO courseDAO;
    private LessonDAO lessonDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        lessonDAO = new LessonDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

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
                deleteLesson(request, response);
                break;
            default:
                showLessonList(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = trimToEmpty(request.getParameter("action"));
        switch (action) {
            case "create":
                createLesson(request, response);
                break;
            case "update":
                updateLesson(request, response);
                break;
            case "delete":
                deleteLesson(request, response);
                break;
            default:
                setFlashError(request, "Thao tác không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/admin/courses");
                break;
        }
    }

    private void showLessonList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer courseId = parseInteger(request.getParameter("courseId"));
        Course course = getCourseOrRedirect(courseId, request, response);
        if (course == null) {
            return;
        }

        List<Lesson> lessons = lessonDAO.findByCourseId(courseId);

        consumeFlashMessages(request);
        request.setAttribute("course", course);
        request.setAttribute("lessons", lessons);
        request.setAttribute("totalLessons", lessons.size());
        request.setAttribute("selectedCourseId", courseId);
        request.setAttribute("contentTypes", CONTENT_TYPES);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/lesson-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer courseId = parseInteger(request.getParameter("courseId"));
        Course course = getCourseOrRedirect(courseId, request, response);
        if (course == null) {
            return;
        }

        Lesson lesson = new Lesson();
        lesson.setCourseId(courseId);
        lesson.setLessonOrder(getNextLessonOrder(courseId));
        lesson.setContentType(CONTENT_TYPE_TEXT);
        lesson.setStatus(ACTIVE_STATUS);

        request.setAttribute("action", "create");
        prepareFormData(request, course, lesson);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/lesson-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer lessonId = parseInteger(request.getParameter("id"));
        if (lessonId == null || lessonId <= 0) {
            setFlashError(request, "Mã bài học không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        Lesson lesson = lessonDAO.findById(lessonId);
        if (lesson == null) {
            setFlashError(request, "Bài học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        Course course = courseDAO.findById(lesson.getCourseId());
        if (course == null) {
            setFlashError(request, "Khóa học của bài học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        request.setAttribute("action", "update");
        prepareFormData(request, course, lesson);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/lesson-form.jsp");
        dispatcher.forward(request, response);
    }

    private void createLesson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Lesson lesson = buildLessonFromRequest(request);
        String validationError = validateLesson(lesson, null);
        if (validationError != null) {
            forwardToFormWithError(request, response, "create", lesson, validationError);
            return;
        }

        boolean created = lessonDAO.insertLesson(lesson);
        if (created) {
            setFlashSuccess(request, "Thêm bài học thành công.");
            response.sendRedirect(buildLessonListUrl(request, lesson.getCourseId()));
        } else {
            forwardToFormWithError(request, response, "create", lesson, "Không thể thêm bài học. Vui lòng thử lại.");
        }
    }

    private void updateLesson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer lessonId = parseInteger(request.getParameter("id"));
        if (lessonId == null || lessonId <= 0) {
            setFlashError(request, "Mã bài học không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        Lesson existingLesson = lessonDAO.findById(lessonId);
        if (existingLesson == null) {
            setFlashError(request, "Bài học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        Lesson lesson = buildLessonFromRequest(request);
        lesson.setLessonId(lessonId);

        String validationError = validateLesson(lesson, lessonId);
        if (validationError != null) {
            forwardToFormWithError(request, response, "update", lesson, validationError);
            return;
        }

        boolean updated = lessonDAO.updateLesson(lesson);
        if (updated) {
            setFlashSuccess(request, "Cập nhật bài học thành công.");
            response.sendRedirect(buildLessonListUrl(request, lesson.getCourseId()));
        } else {
            forwardToFormWithError(request, response, "update", lesson, "Không thể cập nhật bài học. Vui lòng thử lại.");
        }
    }

    private void deleteLesson(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer lessonId = parseInteger(request.getParameter("id"));
        if (lessonId == null || lessonId <= 0) {
            setFlashError(request, "Mã bài học không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        Lesson lesson = lessonDAO.findById(lessonId);
        if (lesson == null) {
            setFlashError(request, "Bài học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        int courseId = lesson.getCourseId();
        boolean deleted = lessonDAO.deleteLesson(lessonId);
        if (deleted) {
            setFlashSuccess(request, "Xóa bài học thành công.");
        } else {
            setFlashError(request, "Không thể xóa bài học. Vui lòng thử lại.");
        }

        response.sendRedirect(buildLessonListUrl(request, courseId));
    }

    private Lesson buildLessonFromRequest(HttpServletRequest request) {
        Lesson lesson = new Lesson();
        lesson.setCourseId(parseIntegerOrDefault(request.getParameter("courseId"), 0));
        lesson.setTitle(trimToEmpty(request.getParameter("title")));
        lesson.setLessonOrder(parseIntegerOrDefault(request.getParameter("lessonOrder"), 0));
        lesson.setContentType(normalizeContentType(request.getParameter("contentType")));
        lesson.setContentValue(trimToEmpty(request.getParameter("contentValue")));
        lesson.setStatus(parseStatus(request.getParameter("status")));
        return lesson;
    }

    private String validateLesson(Lesson lesson, Integer currentLessonId) {
        if (lesson.getCourseId() <= 0 || courseDAO.findById(lesson.getCourseId()) == null) {
            return "Vui lòng chọn khóa học hợp lệ.";
        }

        if (lesson.getTitle() == null || lesson.getTitle().trim().isEmpty()) {
            return "Tên bài học không được để trống.";
        }

        if (lesson.getTitle().length() > 150) {
            return "Tên bài học không được vượt quá 150 ký tự.";
        }

        if (lesson.getLessonOrder() <= 0) {
            return "Thứ tự bài học phải lớn hơn 0.";
        }

        if (!CONTENT_TYPES.contains(lesson.getContentType())) {
            return "Loại nội dung bài học không hợp lệ.";
        }

        if (lesson.getContentValue() != null && lesson.getContentValue().length() > 5000) {
            return "Nội dung bài học không được vượt quá 5000 ký tự.";
        }

        if (isDuplicateLessonOrder(lesson, currentLessonId)) {
            return "Thứ tự bài học đã tồn tại trong khóa học này.";
        }

        return null;
    }

    private boolean isDuplicateLessonOrder(Lesson lesson, Integer currentLessonId) {
        List<Lesson> lessons = lessonDAO.findByCourseId(lesson.getCourseId());
        for (Lesson existingLesson : lessons) {
            if (currentLessonId != null && existingLesson.getLessonId() == currentLessonId) {
                continue;
            }

            if (existingLesson.getLessonOrder() == lesson.getLessonOrder()) {
                return true;
            }
        }

        return false;
    }

    private int getNextLessonOrder(int courseId) {
        List<Lesson> lessons = lessonDAO.findByCourseId(courseId);
        int maxOrder = 0;
        for (Lesson lesson : lessons) {
            if (lesson.getLessonOrder() > maxOrder) {
                maxOrder = lesson.getLessonOrder();
            }
        }

        return maxOrder + 1;
    }

    private Course getCourseOrRedirect(Integer courseId, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (courseId == null || courseId <= 0) {
            setFlashError(request, "Vui lòng chọn khóa học để quản lý bài học.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return null;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            setFlashError(request, "Khóa học không tồn tại.");
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return null;
        }

        return course;
    }

    private void prepareFormData(HttpServletRequest request, Course course, Lesson lesson) {
        consumeFlashMessages(request);
        request.setAttribute("course", course);
        request.setAttribute("lesson", lesson);
        request.setAttribute("courses", courseDAO.findAll());
        request.setAttribute("contentTypes", CONTENT_TYPES);
    }

    private void forwardToFormWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String action,
            Lesson lesson,
            String error
    ) throws ServletException, IOException {
        Course course = courseDAO.findById(lesson.getCourseId());
        if (course == null) {
            setFlashError(request, error);
            response.sendRedirect(request.getContextPath() + "/admin/courses");
            return;
        }

        request.setAttribute("error", error);
        request.setAttribute("action", action);
        prepareFormData(request, course, lesson);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/lesson-form.jsp");
        dispatcher.forward(request, response);
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

    private String normalizeContentType(String contentType) {
        String normalizedContentType = trimToEmpty(contentType).toUpperCase(Locale.ROOT);
        if (CONTENT_TYPES.contains(normalizedContentType)) {
            return normalizedContentType;
        }

        return CONTENT_TYPE_TEXT;
    }

    private int parseStatus(String value) {
        Integer status = parseInteger(value);
        if (status == null) {
            return ACTIVE_STATUS;
        }

        return status == INACTIVE_STATUS ? INACTIVE_STATUS : ACTIVE_STATUS;
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

    private String buildLessonListUrl(HttpServletRequest request, int courseId) {
        return request.getContextPath() + "/admin/lessons?courseId=" + courseId;
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
