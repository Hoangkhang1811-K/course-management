package controller;

import dao.CategoryDAO;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.LessonDAO;
import model.Category;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CourseDetailServlet", urlPatterns = {"/course-detail", "/courses/detail"})
public class CourseDetailServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int ACTIVE_STATUS = 1;
    private static final int RELATED_COURSE_LIMIT = 4;

    private CourseDAO courseDAO;
    private CategoryDAO categoryDAO;
    private LessonDAO lessonDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        categoryDAO = new CategoryDAO();
        lessonDAO = new LessonDAO();
        enrollmentDAO = new EnrollmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer courseId = getCourseId(request);
        if (courseId == null) {
            response.sendRedirect(request.getContextPath() + "/views/error/404.jsp");
            return;
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            response.sendRedirect(request.getContextPath() + "/views/error/404.jsp");
            return;
        }

        User loggedInUser = getLoggedInUser(request);
        boolean admin = isAdmin(loggedInUser);
        if (course.getStatus() != ACTIVE_STATUS && !admin) {
            response.sendRedirect(request.getContextPath() + "/views/error/404.jsp");
            return;
        }

        Category category = categoryDAO.findById(course.getCategoryId());
        List<Lesson> lessons = lessonDAO.findByCourseId(course.getCourseId());
        List<Lesson> visibleLessons = getVisibleLessons(lessons, admin);
        List<Course> relatedCourses = getRelatedCourses(course, admin);

        boolean loggedIn = loggedInUser != null;
        boolean enrolled = loggedIn
                && enrollmentDAO.existsByUserAndCourse(loggedInUser.getUserId(), course.getCourseId());

        request.setAttribute("course", course);
        request.setAttribute("category", category);
        request.setAttribute("lessons", visibleLessons);
        request.setAttribute("totalLessons", visibleLessons.size());
        request.setAttribute("relatedCourses", relatedCourses);
        request.setAttribute("loggedIn", loggedIn);
        request.setAttribute("enrolled", enrolled);
        request.setAttribute("canEnroll", loggedIn && !admin && !enrolled && course.getStatus() == ACTIVE_STATUS);
        request.setAttribute("loginUrl", request.getContextPath() + "/login");
        request.setAttribute("enrollUrl", request.getContextPath() + "/enroll?courseId=" + course.getCourseId());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/client/course-detail.jsp");
        dispatcher.forward(request, response);
    }

    private Integer getCourseId(HttpServletRequest request) {
        String value = request.getParameter("id");
        if (value == null || value.trim().isEmpty()) {
            value = request.getParameter("courseId");
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

    private boolean isAdmin(User user) {
        return user != null && user.getRoleId() == ADMIN_ROLE_ID;
    }

    private List<Lesson> getVisibleLessons(List<Lesson> lessons, boolean admin) {
        if (admin) {
            return lessons;
        }

        List<Lesson> visibleLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getStatus() == ACTIVE_STATUS) {
                visibleLessons.add(lesson);
            }
        }

        return visibleLessons;
    }

    private List<Course> getRelatedCourses(Course currentCourse, boolean admin) {
        List<Course> coursesInSameCategory = courseDAO.findByCategory(currentCourse.getCategoryId());
        List<Course> relatedCourses = new ArrayList<>();

        for (Course course : coursesInSameCategory) {
            if (course.getCourseId() == currentCourse.getCourseId()) {
                continue;
            }

            if (!admin && course.getStatus() != ACTIVE_STATUS) {
                continue;
            }

            relatedCourses.add(course);
            if (relatedCourses.size() == RELATED_COURSE_LIMIT) {
                break;
            }
        }

        return relatedCourses;
    }

    private Integer parseInteger(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
