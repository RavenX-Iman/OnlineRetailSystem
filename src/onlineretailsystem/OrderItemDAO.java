package onlineretailsystem;

import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public OrderItemDAO() {
        // Default constructor
    }

    // Insert a new OrderItem
    public void insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO OrderItems_table (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert order item");
        }
    }

    // Fetch all OrderItems
    public List<OrderItem> getAllOrderItems() {
        String sql = "SELECT * FROM OrderItems_table";
        List<OrderItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch all order items");
        }
        return items;
    }

    // Delete OrderItem by ID
    public boolean deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM OrderItems_table WHERE OrderItemID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete order item with ID " + orderItemId);
            return false;
        }
    }

    // Get OrderItems by Order ID
    public List<OrderItem> getItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM OrderItems_table WHERE OrderID = ?";
        List<OrderItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("OrderItemID"));

                    Order order = new Order();
                    order.setOrderId(rs.getInt("OrderID"));
                    item.setOrder(order);

                    int productId = rs.getInt("ProductID");
                    ProductDAO productDAO = new ProductDAO(conn);
                    Product product = productDAO.getProductById(productId);
                    item.setProduct(product);

                    item.setQuantity(rs.getInt("Quantity"));
                    item.setPrice(rs.getBigDecimal("Price"));

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch order items for order ID " + orderId);
        }
        return items;
    }

    // Update OrderItem
    public boolean updateOrderItem(OrderItem item) {
        String sql = "UPDATE OrderItems_table SET OrderID = ?, ProductID = ?, Quantity = ?, Price = ? WHERE OrderItemID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getOrder().getOrderId());
            stmt.setInt(2, item.getProduct().getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.setInt(5, item.getOrderItemId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update order item with ID " + item.getOrderItemId());
            return false;
        }
    }
}
