package Service;

import onlineretailsystem.OrderDAO;
import onlineretailsystem.ModelClasses.Order;
import java.util.List;

public class OrderService {
    private final OrderDAO dao = new OrderDAO();

    // Retrieves all orders with totals (handled in DAO)
    public List<Order> getAllOrdersWithTotal() {
        return dao.getAllOrders(); // This method exists and is correctly implemented
    }

    // Places a new order into the database
    public boolean placeOrder(Order order) {
        return dao.insertOrder(order) > 0; // insertOrder returns number of rows inserted
    }

    // Optional: Get a specific order by ID
    public Order getOrderById(int orderId) {
        return dao.getOrderById(orderId);
    }

    // Optional: Delete order by ID
    public boolean deleteOrder(int orderId) {
        return dao.deleteOrder(orderId) > 0;
    }

    // Optional: Update order total
    public boolean updateOrderTotal(int orderId, java.math.BigDecimal newTotal) {
        return dao.updateOrderTotal(orderId, newTotal) > 0;
    }

    // Optional: Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        return dao.updateOrderStatus(orderId, status) > 0;
    }

    public void closeDAO() {
        dao.close(); // Ensure to close the DAO connection
    }
}
