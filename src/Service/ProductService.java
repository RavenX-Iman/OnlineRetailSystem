package Service;

import onlineretailsystem.ProductDAO;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.DBConnection;

import java.sql.Connection;
import java.util.List;

public class ProductService {
    private  ProductDAO dao;
     public ProductService() {
        // Optional: initialize dependencies here
    }


    // Constructor receives and passes Connection object
    public ProductService(Connection connection) {
        this.dao = new ProductDAO(connection);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return dao.getAllProducts();
    }

    // Add a new product (returns true if successful)
    public boolean addProduct(Product product) {
        try {
            // Check for duplicates (optional step if desired)
            boolean exists = dao.productExists(product.getProductName(), product.getCategory().getCategoryId());
            if (exists) {
                System.out.println("Product with same name and category already exists.");
                return false;
            }

            dao.insertProduct(product);
            return true;
        } catch (Exception e) {
            System.err.println("Error while adding product: " + e.getMessage());
            return false;
        }
    }
      public void close() {
        dao.closeDAO();
    }
    
}
