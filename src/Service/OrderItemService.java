package Service;

import onlineretailsystem.OrderItemDAO;
import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

public class OrderItemService {
    private final OrderItemDAO dao;
    private final Connection connection;

    public OrderItemService() {
        try {
            this.connection = DBConnection.getConnection();
            this.dao = new OrderItemDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public List<OrderItem> getAllOrderItems() {
        return dao.getAllOrderItems();
    }

    public boolean addOrderItem(OrderItem orderItem) {
        if (orderItem == null || orderItem.getQuantity() <= 0 || orderItem.getPrice() == null) {
            return false;
        }
        dao.insertOrderItem(orderItem);
        return true;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return dao.getItemsByOrderId(orderId);
    }

    public boolean updateOrderItemQuantity(int orderItemId, int newQuantity) {
        if (newQuantity <= 0) return false;
        OrderItem item = new OrderItem();
        item.setOrderItemId(orderItemId);
        item.setQuantity(newQuantity);
        return dao.updateOrderItem(item);
    }

    public boolean updateOrderItemPrice(int orderItemId, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) return false;
        OrderItem item = new OrderItem();
        item.setOrderItemId(orderItemId);
        item.setPrice(newPrice);
        return dao.updateOrderItem(item);
    }

    public boolean deleteOrderItem(int orderItemId) {
        return dao.deleteOrderItem(orderItemId);
    }

    public void close() {
        DBConnection.closeConnection(connection);
    }
}