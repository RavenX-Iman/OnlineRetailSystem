package onlineretailsystem;

import onlineretailsystem.ModelClasses.Category;
import java.util.List;

public class TestCategoryDAO {
    public static void main(String[] args) {
        CategoryDAO dao = new CategoryDAO();

        // Test insert (optional — skip if you already inserted via SQL)
        Category newCategory = new Category(0, "Stationery", "Pens, notebooks, and office supplies");
        dao.insertCategory(newCategory);

        // Test fetch
        List<Category> categories = dao.getAllCategories();
        for (Category c : categories) {
            System.out.println(c);
        }
    }
}
