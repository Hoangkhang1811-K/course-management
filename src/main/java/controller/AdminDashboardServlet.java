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
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = "/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int ACTIVE_STATUS = 1;
    private static final int RECENT_LIMIT = 5;

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
        User loggedInUser = getLoggedInUser(request);

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (loggedInUser.getRoleId() != ADMIN_ROLE_ID) {
            response.sendRedirect(request.getContextPath() + "/views/error/403.jsp");
            return;
        }

        List<Course> courses = courseDAO.findAll();
        List<Category> categories = categoryDAO.findAll();
        List<User> users = userDAO.findAll();
        List<User> students = userDAO.findAllStudents();

        request.setAttribute("totalCourses", courses.size());
        request.setAttribute("activeCourses", countActiveCourses(courses));
        request.setAttribute("totalCategories", categories.size());
        request.setAttribute("activeCategories", countActiveCategories(categories));
        request.setAttribute("totalUsers", users.size());
        request.setAttribute("totalStudents", students.size());
        request.setAttribute("recentCourses", getRecentCourses(courses));
        request.setAttribute("recentStudents", getRecentStudents(students));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/dashboard.jsp");
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

    private long countActiveCourses(List<Course> courses) {
        return courses.stream()
                .filter(course -> course.getStatus() == ACTIVE_STATUS)
                .count();
    }

    private long countActiveCategories(List<Category> categories) {
        return categories.stream()
                .filter(category -> category.getStatus() == ACTIVE_STATUS)
                .count();
    }

    private List<Course> getRecentCourses(List<Course> courses) {
        return courses.subList(0, Math.min(RECENT_LIMIT, courses.size()));
    }

    private List<User> getRecentStudents(List<User> students) {
        return students.subList(0, Math.min(RECENT_LIMIT, students.size()));
    }
}
