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
}
