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
            System.out.println("Error fetching categories: " + e.getMessage());
        }

        return categories;
    }

    // Get category by ID
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Categories_table WHERE CategoryID = ?";
        Category category = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
            System.out.println("Error fetching category by ID: " + e.getMessage());
        }

        return category;
    }

    // Insert a new category
    public void insertCategory(Category category) {
        if (category == null || category.getCategoryName() == null) {
            System.out.println("Invalid category data.");
            return;
        }

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
    
// Update an existing category
public void updateCategory(Category category) {
    if (category == null || category.getCategoryId() <= 0) {
        System.out.println("Invalid category data.");
        return;
    }

    String sql = "UPDATE Categories_table SET CategoryName = ?, Description = ? WHERE CategoryID = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, category.getCategoryName());
        stmt.setString(2, category.getDescription());
        stmt.setInt(3, category.getCategoryId());

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Category updated successfully.");
        } else {
            System.out.println("No category found with the provided ID.");
        }
        System.out.println("Updated " + rowsUpdated + " category(s).");

    } catch (SQLException e) {
        System.out.println("Update failed: " + e.getMessage());
    }
}

// Delete category by ID
public void deleteCategory(int categoryId) {
    String sql = "DELETE FROM Categories_table WHERE CategoryID = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, categoryId);

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Category deleted successfully.");
        } else {
            System.out.println("No category found with the provided ID.");
        }
        System.out.println("Deleted " + rowsDeleted + " category(s).");

    } catch (SQLException e) {
        System.out.println("Delete failed: " + e.getMessage());
    }
}
}
