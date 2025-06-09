package onlineretailsystem;

import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class TestOrderItemDAO {

    public static void main(String[] args) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();

        // 🔹 Assume valid OrderID and ProductID exist in the DB
        Order order = new Order();
        order.setOrderId(1); // Replace with valid ID from your DB

        Product product = new Product();
        product.setProductId(1); // Replace with valid ID from your DB
        product.setPrice(new BigDecimal("50.00")); // Manually set for subtotal calculation

        // 🔹 Create and insert a new OrderItem
        OrderItem item = new OrderItem(order, product, 2); // 2 units
        orderItemDAO.insertOrderItem(item);
        System.out.println("Inserted OrderItem ID: " + item.getOrderItemId());

        // 🔹 Get all order items
        List<OrderItem> allItems = orderItemDAO.getAllOrderItems();
        System.out.println("\nAll OrderItems:");
        for (OrderItem i : allItems) {
            System.out.println(i);
        }

        // 🔹 Update the inserted item
        item.setQuantity(3);
        item.setPrice(new BigDecimal("45.00"));
        boolean updated = orderItemDAO.updateOrderItem(item);
        System.out.println("\nUpdated OrderItem: " + updated);

        // 🔹 Get by OrderID
        List<OrderItem> orderItemsByOrderId = orderItemDAO.getItemsByOrderId(order.getOrderId());
        System.out.println("\nItems for Order ID " + order.getOrderId() + ":");
        for (OrderItem i : orderItemsByOrderId) {
            System.out.println(i);
        }

        // 🔹 Delete the inserted item
        boolean deleted = orderItemDAO.deleteOrderItem(item.getOrderItemId());
        System.out.println("\nDeleted OrderItem: " + deleted);
    }
}
