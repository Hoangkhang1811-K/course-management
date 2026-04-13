package controller;

import dao.CategoryDAO;
import model.Category;
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

@WebServlet(name = "CategoryServle", urlPatterns = "/admin/categories")
public class CategoryServle extends HttpServlet {
    private static final int ADMIN_ROLE_ID = 1;
    private static final int ACTIVE_STATUS = 1;
    private static final int INACTIVE_STATUS = 0;

    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
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
                deleteCategory(request, response);
                break;
            default:
                showCategoryList(request, response);
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

        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateCategory(request, response);
        } else {
            createCategory(request, response);
        }
    }

    private void showCategoryList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/category-list.jsp");
        dispatcher.forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "create");
        request.setAttribute("category", new Category("", "", ACTIVE_STATUS));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/category-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer categoryId = parseInteger(request.getParameter("id"));
        if (categoryId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?error=invalid-id");
            return;
        }

        Category category = categoryDAO.findById(categoryId);
        if (category == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?error=not-found");
            return;
        }

        request.setAttribute("action", "update");
        request.setAttribute("category", category);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/category-form.jsp");
        dispatcher.forward(request, response);
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Category category = buildCategoryFromRequest(request);
        String validationError = validateCategory(category);

        if (validationError != null) {
            request.setAttribute("error", validationError);
            request.setAttribute("action", "create");
            request.setAttribute("category", category);
            forwardToForm(request, response);
            return;
        }

        boolean created = categoryDAO.insertCategory(category);
        if (created) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?success=created");
        } else {
            request.setAttribute("error", "Khong the them danh muc. Vui long thu lai.");
            request.setAttribute("action", "create");
            request.setAttribute("category", category);
            forwardToForm(request, response);
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer categoryId = parseInteger(request.getParameter("id"));
        if (categoryId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?error=invalid-id");
            return;
        }

        Category category = buildCategoryFromRequest(request);
        category.setCategoryId(categoryId);

        String validationError = validateCategory(category);
        if (validationError != null) {
            request.setAttribute("error", validationError);
            request.setAttribute("action", "update");
            request.setAttribute("category", category);
            forwardToForm(request, response);
            return;
        }

        boolean updated = categoryDAO.updateCategory(category);
        if (updated) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?success=updated");
        } else {
            request.setAttribute("error", "Khong the cap nhat danh muc. Vui long thu lai.");
            request.setAttribute("action", "update");
            request.setAttribute("category", category);
            forwardToForm(request, response);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer categoryId = parseInteger(request.getParameter("id"));
        if (categoryId == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?error=invalid-id");
            return;
        }

        boolean deleted = categoryDAO.deleteCategory(categoryId);
        if (deleted) {
            response.sendRedirect(request.getContextPath() + "/admin/categories?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/categories?error=delete-failed");
        }
    }

    private Category buildCategoryFromRequest(HttpServletRequest request) {
        String name = trimToEmpty(request.getParameter("name"));
        String description = trimToEmpty(request.getParameter("description"));
        int status = parseStatus(request.getParameter("status"));

        return new Category(name, description, status);
    }

    private String validateCategory(Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return "Ten danh muc khong duoc de trong.";
        }

        if (category.getName().length() > 50) {
            return "Ten danh muc khong duoc vuot qua 50 ky tu.";
        }

        if (category.getDescription() != null && category.getDescription().length() > 500) {
            return "Mo ta khong duoc vuot qua 500 ky tu.";
        }

        return null;
    }

    private void forwardToForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/admin/category-form.jsp");
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

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
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
