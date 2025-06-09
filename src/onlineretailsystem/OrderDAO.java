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

                ModelClasses.Customer customer = new CustomerDAO().getCustomerById(customerId);
                ModelClasses.Order order = new ModelClasses.Order(customer);
                order.setOrderId(id);
                order.setTotalAmount(total);
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

                ModelClasses.Customer customer = new CustomerDAO().getCustomerById(customerId);
                order = new ModelClasses.Order(customer);
                order.setOrderId(id);
                order.setTotalAmount(total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public int insertOrder(ModelClasses.Order order) {
        String sql = "INSERT INTO Orders_table (CustomerID, Orderdate, TotalAmount) VALUES (?, ?, ?)";
        int rows = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getCustomer().getCustomerId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setBigDecimal(3, order.getTotalAmount());

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
