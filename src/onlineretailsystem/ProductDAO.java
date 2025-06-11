package onlineretailsystem;

import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.Category;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class ProductDAO {
    private final Connection conn;
    private final CategoryDAO categoryDAO;

    public ProductDAO(Connection conn) {
        this.conn = conn;
        this.categoryDAO = new CategoryDAO(conn);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products_table";

        List<Category> categories = categoryDAO.getAllCategories();
        Map<Integer, Category> categoryMap = new HashMap<>();
        for (Category cat : categories) {
            categoryMap.put(cat.getCategoryId(), cat);
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryId = rs.getInt("CategoryID");
                BigDecimal price = rs.getBigDecimal("Price");
                int stock = rs.getInt("Stock");
                Timestamp createdAt = rs.getTimestamp("createdat");

                Category category = categoryMap.get(categoryId);
                if (category == null) {
                    System.out.println("Warning: Category not found for ID: " + categoryId);
                    continue;
                }

                Product product = new Product(productName, category, price, stock);
                product.setProductId(productId);
                product.setCreatedAt(createdAt.toLocalDateTime());

                products.add(product);
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "retrieve products");
        }

        return products;
    }

    public boolean productExists(String name, int categoryId) {
        String sql = "SELECT COUNT(*) FROM Products_table WHERE ProductName = ? AND CategoryID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "check if product exists");
        }
        return false;
    }

    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO Products_table (ProductName, CategoryID, Price, Stock) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getCategory().getCategoryId());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        product.setProductId(keys.getInt(1));
                    }
                }
                System.out.println("Inserted " + rows + " product(s).");
                return true;
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert product");
        }
        return false;
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM Products_table WHERE ProductID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            DBErrorHandler.handle(e, "fetch product by ID");
        }

        return null;
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Products_table SET ProductName = ?, CategoryID = ?, Price = ?, Stock = ? WHERE ProductID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getCategory().getCategoryId());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getProductId());

            int rows = stmt.executeUpdate();
            System.out.println("Updated " + rows + " product(s).");

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update product");
        }
    }

    public void deleteProduct(int productId) {
        String sql = "DELETE FROM Products_table WHERE ProductID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Deleted " + rows + " product(s).");
            } else {
                System.out.println("No product found with ID: " + productId);
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete product");
        }
    }
    public void closeDAO() {
    try {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    } catch (SQLException e) {
        DBErrorHandler.handle(e, "close ProductDAO");
    }
}
}
