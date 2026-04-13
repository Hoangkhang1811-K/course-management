package controller;

import dao.CategoryDAO;
import dao.CourseDAO;
import model.Category;
import model.Course;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses", "/course-list"})
public class CourseServlet extends HttpServlet {
    private static final int ACTIVE_STATUS = 1;

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = trimToEmpty(request.getParameter("keyword"));
        Integer categoryId = parseInteger(request.getParameter("categoryId"));

        List<Category> categories = getActiveCategories(categoryDAO.findAll());
        List<Course> courses;

        if (!keyword.isEmpty()) {
            courses = courseDAO.searchByTitle(keyword);
        } else if (categoryId != null && categoryId > 0) {
            Category category = categoryDAO.findById(categoryId);
            if (category == null || category.getStatus() != ACTIVE_STATUS) {
                request.setAttribute("error", "Danh mục khóa học không tồn tại hoặc đã bị khóa.");
                courses = new ArrayList<>();
            } else {
                courses = courseDAO.findByCategory(categoryId);
                request.setAttribute("selectedCategory", category);
            }
        } else {
            courses = courseDAO.findAll();
        }

        courses = getActiveCourses(courses);

        if (courses.isEmpty() && request.getAttribute("error") == null) {
            request.setAttribute("message", "Không tìm thấy khóa học phù hợp.");
        }

        request.setAttribute("courses", courses);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("totalCourses", courses.size());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/client/course-list.jsp");
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

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
