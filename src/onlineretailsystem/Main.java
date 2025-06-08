package onlineretailsystem;
public class Main {
    public static void main(String[] args) {

        // Factory Pattern
        RetailFactory factory = new SimpleRetailFactory();
        ModelClasses.Customer customer = (ModelClasses.Customer) factory.create("customer");
        ModelClasses.Product product = (ModelClasses.Product) factory.create("product");

        // Singleton InventoryManager
        InventoryManager inventory = InventoryManager.getInstance();
        inventory.updateStock(product, 20);

        // Observer Pattern for Order status notification
        ObservableOrder order = new ObservableOrder(customer);
        order.addObserver(new EmailObserver());
        order.addObserver(new SMSObserver());

        System.out.println("Order status before: " + order.getStatus());
        order.setStatus(ModelClasses.Order.OrderStatus.SHIPPED); // triggers notifications

        // Strategy Pattern for shipping
        ShippingContext shippingContext = new ShippingContext(new FlatRateShipping());
        System.out.println("Shipping cost: $" + shippingContext.getShippingCost(5));
        shippingContext.setStrategy(new WeightBasedShipping());
        System.out.println("Shipping cost: $" + shippingContext.getShippingCost(5));
    }
}
