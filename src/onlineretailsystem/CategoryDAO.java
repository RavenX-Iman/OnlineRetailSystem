package onlineretailsystem;

import onlineretailsystem.ModelClasses.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Retrieve all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories_table";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
            System.out.println("Query failed: " + e.getMessage());
        }

        return categories;
    }

    // Insert a new category
    public void insertCategory(Category category) {
        String sql = "INSERT INTO Categories_table (CategoryName, Description) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Inserted " + rowsInserted + " category(s).");

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public void updateCategory(Category category) {
        String sql = "UPDATE Categories_table SET CategoryName = ?, Description = ? WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated " + rowsUpdated + " category(s).");

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Categories_table WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " category(s).");

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}
