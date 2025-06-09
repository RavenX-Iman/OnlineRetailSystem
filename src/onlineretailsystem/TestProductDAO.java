package onlineretailsystem;

import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.ModelClasses.Product;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;

public class TestProductDAO {
    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        // 1. Test getAllProducts()
        System.out.println("Fetching all products:");
        List<Product> productList = productDAO.getAllProducts();
        for (Product p : productList) {
            System.out.println("ID: " + p.getProductId() +
                               ", Name: " + p.getProductName() +
                               ", Category: " + p.getCategory().getCategoryName() +
                               ", Price: " + p.getPrice() +
                               ", Stock: " + p.getStock() +
                               ", Created At: " + p.getCreatedAt());
        }

        // 2. Test insertProduct()
        Category category = categoryDAO.getCategoryById(1); // Ensure ID 1 exists
        if (category != null) {
            Product newProduct = new Product("Wireless Mouse", category, new BigDecimal("25.99"), 50);
            productDAO.insertProduct(newProduct);
            System.out.println("Inserted Product ID: " + newProduct.getProductId());
        } else {
            System.out.println("Category not found. Cannot insert product.");
        }

        // 3. Test getProductById()
        int testId = 1; // Use a valid ProductID
        Product product = productDAO.getProductById(testId);
        if (product != null) {
            System.out.println("Fetched product with ID " + testId + ": " + product.getProductName());
        } else {
            System.out.println("Product not found with ID " + testId);
        }

        // 4. Test updateProduct()
        if (product != null) {
            product.setPrice(new BigDecimal("29.99"));
            product.setStock(75);
            productDAO.updateProduct(product);
            System.out.println("Updated product ID " + product.getProductId());

            // Re-fetch to verify update
            Product updated = productDAO.getProductById(product.getProductId());
            System.out.println("After update: Price=" + updated.getPrice() + ", Stock=" + updated.getStock());
        }

        // 5. Test deleteProduct()
        int deleteId = 2; // Make sure this product exists if you want to test deletion
        productDAO.deleteProduct(deleteId);
    }
}
