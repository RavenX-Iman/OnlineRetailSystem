package onlineretailsystem;

import model.ModelClasses.OrderItem;
import model.ModelClasses.Order;
import model.ModelClasses.Product;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    private final Connection connection;

    public OrderItemDAO(Connection connection) {
        this.connection = connection;
    }

    // Insert a new OrderItem
    public void insertOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO OrderItems_table (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getOrder().getOrderId());
            stmt.setInt(2, item.getProduct().getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setOrderItemId(rs.getInt(1));
                }
            }
        }
    }

    // Fetch all OrderItems
    public List<OrderItem> getAllOrderItems() throws SQLException {
        String sql = "SELECT * FROM OrderItems_table";
        List<OrderItem> items = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("OrderItemID"));

                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                item.setOrder(order);

                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                item.setProduct(product);

                item.setQuantity(rs.getInt("Quantity"));
                item.setPrice(rs.getBigDecimal("Price"));

                items.add(item);
            }
        }
        return items;
    }

    // Delete OrderItem by ID
    public boolean deleteOrderItem(int orderItemId) throws SQLException {
        String sql = "DELETE FROM OrderItems_table WHERE OrderItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Get OrderItems by OrderID
    public List<OrderItem> getItemsByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM OrderItems_table WHERE OrderID = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("OrderItemID"));

                    Order order = new Order();
                    order.setOrderId(rs.getInt("OrderID"));
                    item.setOrder(order);

                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    item.setProduct(product);

                    item.setQuantity(rs.getInt("Quantity"));
                    item.setPrice(rs.getBigDecimal("Price"));

                    items.add(item);
                }
            }
        }
        return items;
    }

    // Optional: Update OrderItem
    public boolean updateOrderItem(OrderItem item) throws SQLException {
        String sql = "UPDATE OrderItems_table SET OrderID = ?, ProductID = ?, Quantity = ?, Price = ? WHERE OrderItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, item.getOrder().getOrderId());
            stmt.setInt(2, item.getProduct().getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.setInt(5, item.getOrderItemId());
            return stmt.executeUpdate() > 0;
        }
    }
}
