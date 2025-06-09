package onlineretailsystem;

import onlineretailsystem.ModelClasses.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public CategoryDAO() {
        Connection tempConn = null;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "initialize CategoryDAO");
        }
        this.conn = tempConn;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories_table";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
                );
                categories.add(category);
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch categories");
        }

        return categories;
    }

    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Categories_table WHERE CategoryID = ?";
        Category category = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category = new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
                    );
                }
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch category by ID");
        }

        return category;
    }

    public void insertCategory(Category category) {
        if (category == null || category.getCategoryName() == null) {
            System.out.println("Invalid category data.");
            return;
        }

        String sql = "INSERT INTO Categories_table (CategoryName, Description) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Inserted " + rowsInserted + " category(s).");

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert category");
        }
    }

    public void updateCategory(Category category) {
        if (category == null || category.getCategoryId() <= 0) {
            System.out.println("Invalid category data.");
            return;
        }

        String sql = "UPDATE Categories_table SET CategoryName = ?, Description = ? WHERE CategoryID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Category updated successfully.");
            } else {
                System.out.println("No category found with the provided ID.");
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update category");
        }
    }

    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Categories_table WHERE CategoryID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("No category found with the provided ID.");
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete category");
        }
    }
}
