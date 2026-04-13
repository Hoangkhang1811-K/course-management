package controller;

import dao.CategoryDAO;
import dao.CourseDAO;
import dao.UserDAO;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/views/home"})
public class HomeServlet extends HttpServlet {
    private static final int ACTIVE_STATUS = 1;
    private static final int FEATURED_COURSE_LIMIT = 6;
    private static final int CATEGORY_LIMIT = 8;

    private CourseDAO courseDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        categoryDAO = new CategoryDAO();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<Course> activeCourses = getActiveCourses(courseDAO.findAll());
        List<Category> activeCategories = getActiveCategories(categoryDAO.findAll());
        List<User> students = userDAO.findAllStudents();

        consumeFlashMessages(request);
        request.setAttribute("featuredCourses", limitCourses(activeCourses, FEATURED_COURSE_LIMIT));
        request.setAttribute("latestCourses", limitCourses(activeCourses, FEATURED_COURSE_LIMIT));
        request.setAttribute("categories", limitCategories(activeCategories, CATEGORY_LIMIT));
        request.setAttribute("totalCourses", activeCourses.size());
        request.setAttribute("totalCategories", activeCategories.size());
        request.setAttribute("totalStudents", countActiveStudents(students));
        request.setAttribute("courseListUrl", request.getContextPath() + "/courses");

        if (activeCourses.isEmpty()) {
            request.setAttribute("message", "Hiện chưa có khóa học nào đang mở.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/client/home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private List<Course> getActiveCourses(List<Course> courses) {
        List<Course> activeCourses = new ArrayList<>();

        for (Course course : courses) {
            if (course.getStatus() == ACTIVE_STATUS) {
                activeCourses.add(course);
            }
        }

        return activeCourses;
    }

    private List<Category> getActiveCategories(List<Category> categories) {
        List<Category> activeCategories = new ArrayList<>();

        for (Category category : categories) {
            if (category.getStatus() == ACTIVE_STATUS) {
                activeCategories.add(category);
            }
        }

        return activeCategories;
    }

    private List<Course> limitCourses(List<Course> courses, int limit) {
        return courses.subList(0, Math.min(limit, courses.size()));
    }

    private List<Category> limitCategories(List<Category> categories, int limit) {
        return categories.subList(0, Math.min(limit, categories.size()));
    }

    private int countActiveStudents(List<User> students) {
        int totalActiveStudents = 0;

        for (User student : students) {
            if (student.getStatus() == ACTIVE_STATUS) {
                totalActiveStudents++;
            }
        }

        return totalActiveStudents;
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
