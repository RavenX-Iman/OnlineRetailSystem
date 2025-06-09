package onlineretailsystem;

import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.Category;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    private CategoryDAO categoryDAO = new CategoryDAO();
    //get all products
    
    public List<Product> getAllProducts() {
        
    List<Product> products = new ArrayList<>();
    String sql = "SELECT * FROM Products_table";

    // Step 1: Load all categories once into a Map
    List<Category> categories = categoryDAO.getAllCategories(); // You must have this method implemented
    Map<Integer, Category> categoryMap = new HashMap<>();
    for (Category cat : categories) {
        categoryMap.put(cat.getCategoryId(), cat);
    }

    // Step 2: Fetch products using a single DB connection
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            int productId = rs.getInt("ProductID");
            String productName = rs.getString("ProductName");
            int categoryId = rs.getInt("CategoryID");
            BigDecimal price = rs.getBigDecimal("Price");
            int stock = rs.getInt("Stock");
            Timestamp createdAt = rs.getTimestamp("createdat");

            // Get category from cache instead of making a new DB call
            Category category = categoryMap.get(categoryId);
            if (category == null) {
                System.out.println("Warning: Category not found for ID: " + categoryId);
                continue; // Skip this product or handle appropriately
            }

            Product product = new Product(productName, category, price, stock);
            product.setProductId(productId);
            product.setCreatedAt(createdAt.toLocalDateTime());

            products.add(product);
        }

        } catch (SQLException e) {
        System.out.println("Error fetching products: " + e.getMessage());
        }

    return products;
    }


    // Insert a new product
    public void insertProduct(Product product) {
        String sql = "INSERT INTO Products_table (ProductName, CategoryID, Price, Stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getCategory().getCategoryId());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        product.setProductId(keys.getInt(1));  // Set the generated ID
                    }
                }
            }

            System.out.println("Inserted " + rows + " product(s).");

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // Get product by ID
    public Product getProductById(int id) {
        String sql = "SELECT * FROM Products_table WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String productName = rs.getString("ProductName");
                    int categoryId = rs.getInt("CategoryID");
                    BigDecimal price = rs.getBigDecimal("Price");
                    int stock = rs.getInt("Stock");
                    Timestamp createdAt = rs.getTimestamp("createdat");

                    Category category = categoryDAO.getCategoryById(categoryId);

                    Product product = new Product(productName, category, price, stock);
                    product.setProductId(id);
                    product.setCreatedAt(createdAt.toLocalDateTime());

                    return product;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }

        return null;
    }

    // Update existing product
    public void updateProduct(Product product) {
        String sql = "UPDATE Products_table SET ProductName = ?, CategoryID = ?, Price = ?, Stock = ? WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getCategory().getCategoryId());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getProductId());

            int rows = stmt.executeUpdate();
            System.out.println("Updated " + rows + " product(s).");

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Delete product by ID
    public void deleteProduct(int productId) {
        String sql = "DELETE FROM Products_table WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);

            int rows = stmt.executeUpdate();
            System.out.println("Deleted " + rows + " product(s).");

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}