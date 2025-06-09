package onlineretailsystem;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<ModelClasses.Order> getAllOrders() {
        List<ModelClasses.Order> orders = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Orders_table")) {

            while (rs.next()) {
                int id = rs.getInt("OrderID");
                int customerId = rs.getInt("CustomerID");
                LocalDateTime date = rs.getTimestamp("Orderdate").toLocalDateTime();
                BigDecimal total = rs.getBigDecimal("TotalAmount");
                String statusStr = rs.getString("Status");

                ModelClasses.Customer customer = new CustomerDAO().getCustomerById(customerId);
                ModelClasses.Order order = new ModelClasses.Order(customer);
                order.setOrderId(id);
                order.setOrderDate(date);
                order.setTotalAmount(total);
                
                // Convert String to OrderStatus enum
                if (statusStr != null) {
                    try {
                        ModelClasses.Order.OrderStatus status = ModelClasses.Order.OrderStatus.valueOf(statusStr.toUpperCase());
                        order.setStatus(status);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Warning: Invalid status '" + statusStr + "' for order " + id + ". Setting to PENDING.");
                        order.setStatus(ModelClasses.Order.OrderStatus.PENDING);
                    }
                }
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public ModelClasses.Order getOrderById(int id) {
        ModelClasses.Order order = null;
        String query = "SELECT * FROM Orders_table WHERE OrderID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                LocalDateTime date = rs.getTimestamp("Orderdate").toLocalDateTime();
                BigDecimal total = rs.getBigDecimal("TotalAmount");
                String statusStr = rs.getString("Status");

                ModelClasses.Customer customer = new CustomerDAO().getCustomerById(customerId);
                order = new ModelClasses.Order(customer);
                order.setOrderId(id);
                order.setOrderDate(date);
                order.setTotalAmount(total);
                
                // Convert String to OrderStatus enum
                if (statusStr != null) {
                    try {
                        ModelClasses.Order.OrderStatus status = ModelClasses.Order.OrderStatus.valueOf(statusStr.toUpperCase());
                        order.setStatus(status);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Warning: Invalid status '" + statusStr + "' for order " + id + ". Setting to PENDING.");
                        order.setStatus(ModelClasses.Order.OrderStatus.PENDING);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public int insertOrder(ModelClasses.Order order) {
        String sql = "INSERT INTO Orders_table (CustomerID, Orderdate, TotalAmount, Status) VALUES (?, ?, ?, ?)";
        int rows = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getCustomer().getCustomerId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getStatus().name()); // Convert enum to String

            rows = stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                order.setOrderId(generatedId);
                System.out.println("Inserted Order ID: " + generatedId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public int updateOrderTotal(int orderId, BigDecimal newTotal) {
        String sql = "UPDATE Orders_table SET TotalAmount = ? WHERE OrderID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, newTotal);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Primary method that accepts OrderStatus enum
    public int updateOrderStatus(int orderId, ModelClasses.Order.OrderStatus status) {
        String sql = "UPDATE Orders_table SET Status = ? WHERE OrderID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name()); // Convert enum to String
            stmt.setInt(2, orderId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Overloaded method for String status (for backward compatibility)
    public int updateOrderStatus(int orderId, String status) {
        try {
            ModelClasses.Order.OrderStatus orderStatus = ModelClasses.Order.OrderStatus.valueOf(status.toUpperCase());
            return updateOrderStatus(orderId, orderStatus);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status: " + status + ". Valid statuses: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED");
            return 0;
        }
    }

    public int deleteOrder(int orderId) {
        String sql = "DELETE FROM Orders_table WHERE OrderID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
        return 0;
    }
}