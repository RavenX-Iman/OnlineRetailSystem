package onlineretailsystem;

import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.Product;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    // Default constructor - consistent with other DAOs
    public OrderItemDAO() {
        // Default constructor
    }

    // Insert a new OrderItem
    public void insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO OrderItems_table (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, item.getOrder().getOrderId());
            stmt.setInt(2, item.getProductName().getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setOrderItemId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Insert OrderItem failed: " + e.getMessage());
        }
    }

    // Fetch all OrderItems
    public List<OrderItem> getAllOrderItems() {
        String sql = "SELECT * FROM OrderItems_table";
        List<OrderItem> items = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("OrderItemID"));

                Order order = new Order();
                order.setOrderId(rs.getInt("OrderID"));
                item.setOrder(order);

                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                item.setProductName(product);

                item.setQuantity(rs.getInt("Quantity"));
                item.setPrice(rs.getBigDecimal("Price"));

                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Get all OrderItems failed: " + e.getMessage());
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
            System.out.println("Delete OrderItem failed: " + e.getMessage());
            return false;
        }
    }

    // Get OrderItems by OrderID
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

                    Product product = new Product();
                    product.setProductId(rs.getInt("ProductID"));
                    item.setProductName(product);

                    item.setQuantity(rs.getInt("Quantity"));
                    item.setPrice(rs.getBigDecimal("Price"));

                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Get OrderItems by OrderID failed: " + e.getMessage());
        }
        return items;
    }

    // Update OrderItem
    public boolean updateOrderItem(OrderItem item) {
        String sql = "UPDATE OrderItems_table SET OrderID = ?, ProductID = ?, Quantity = ?, Price = ? WHERE OrderItemID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, item.getOrder().getOrderId());
            stmt.setInt(2, item.getProductName().getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.setInt(5, item.getOrderItemId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update OrderItem failed: " + e.getMessage());
            return false;
        }
    }
}