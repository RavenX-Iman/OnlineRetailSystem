package onlineretailsystem;

import onlineretailsystem.ModelClasses.Category;
import java.util.List;

public class TestCategoryDAO {
    public static void main(String[] args) {
        CategoryDAO dao = new CategoryDAO();

        // Insert test
        Category newCategory = new Category(0, "Stationery", "Pens, notebooks, and office supplies");
        dao.insertCategory(newCategory);

        // Get All
        System.out.println("\nAll Categories:");
        List<Category> categories = dao.getAllCategories();
        for (Category c : categories) {
            System.out.println(c);
        }

        // Get by ID
        System.out.println("\nCategory with ID 11:");
        Category cat = dao.getCategoryById(11);
        if (cat != null) {
            System.out.println(cat);
        } else {
            System.out.println("Not found.");
        }

        // Update test (e.g., ID 11)
        if (cat != null) {
            cat.setDescription("Updated description for category 1");
            dao.updateCategory(cat);
        }

        // Delete test (e.g., delete last inserted category if needed)
        System.out.println("\nDeleting category with ID 11 (for demo)...");
        dao.deleteCategory(11);
    }
}
