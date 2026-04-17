package service;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    // Lấy danh sách category
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    // Lấy category theo id
    public Category getCategoryById(int categoryId) {
        if (categoryId <= 0) {
            return null;
        }
        return categoryDAO.findById(categoryId);
    }

    // Thêm mới category
    public boolean createCategory(String name, String description, int status) {
        if (!isValidName(name) || !isValidStatus(status)) {
            return false;
        }
        Category category = new Category(
                name.trim(),
                description != null ? description.trim() : "",
                status
        );
        return categoryDAO.insertCategory(category);
    }

    // Cập nhật category
    public boolean updateCategory(int categoryId, String name, String description, int status) {
        if (categoryId <= 0 || !isValidName(name) || !isValidStatus(status)) {
            return false;
        }

        Category existingCategory = categoryDAO.findById(categoryId);
        if (existingCategory == null) {
            return false;
        }

        existingCategory.setName(name.trim());
        existingCategory.setDescription(description != null ? description.trim() : "");
        existingCategory.setStatus(status);

        return categoryDAO.updateCategory(existingCategory);
    }

    // Xóa category
    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }

        Category existingCategory = categoryDAO.findById(categoryId);
        if (existingCategory == null) {
            return false;
        }

        return categoryDAO.deleteCategory(categoryId);
    }

    // Validate tên category
    public boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 100;
    }

    // Validate status
    public boolean isValidStatus(int status) {
        return status == 0 || status == 1;
    }
}
