package Service;

import onlineretailsystem.CategoryDAO;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO dao;
    private final Connection connection;

    public CategoryService() {
        try {
            this.connection = DBConnection.getConnection();
            this.dao = new CategoryDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public List<Category> getAllCategories() {
        return dao.getAllCategories();
    }

    public boolean addCategory(Category category) {
        if (category == null || category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            return false;
        }
        dao.insertCategory(category);
        return true;
    }

    public Category getCategoryById(int categoryId) {
        return dao.getCategoryById(categoryId);
    }

    public boolean updateCategory(Category category) {
        if (category == null || category.getCategoryId() <= 0) {
            return false;
        }
        dao.updateCategory(category);
        return true;
    }

    public boolean deleteCategory(int categoryId) {
        dao.deleteCategory(categoryId);
        return true;
    }

    public void close() {
        DBConnection.closeConnection(connection);
    }
}