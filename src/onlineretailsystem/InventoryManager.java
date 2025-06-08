package onlineretailsystem;
public class InventoryManager {
    private static InventoryManager instance;

    private InventoryManager() {
        System.out.println("InventoryManager instance created");
    }

    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    public void updateStock(ModelClasses.Product product, int quantity) {
        int oldStock = product.getStock();
        product.setStock(oldStock + quantity);
        System.out.println("Stock updated for " + product.getProductName() + ": " + product.getStock());
    }
}


