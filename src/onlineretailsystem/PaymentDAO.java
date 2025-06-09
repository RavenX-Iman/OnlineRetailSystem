package onlineretailsystem;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import onlineretailsystem.ModelClasses.Payment;

public class PaymentDAO {
    private final Connection conn;

    // Constructor that accepts a Connection
    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    // Default constructor: obtains connection internally
    public PaymentDAO() {
        Connection tempConn = null;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "initialize PaymentDAO");
        }
        this.conn = tempConn;
    }

    // Insert a new Payment record
    public boolean insert(Payment payment) {
        String sql = "INSERT INTO Payments_table (OrderID, Amount, PaymentMethod, TransactionID, PaymentDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getOrder().getOrderId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod().name().replace('_', ' '));
            stmt.setString(4, payment.getTransactionId());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Insert failed: No rows affected.");
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setPaymentId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert payment");
            return false;
        }
    }

    // Retrieve all payments
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments_table";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            OrderDAO orderDAO = new OrderDAO(conn);

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("PaymentID"));
                payment.setTransactionId(rs.getString("TransactionID"));
                payment.setPaymentDate(rs.getTimestamp("PaymentDate").toLocalDateTime());
                payment.setAmount(rs.getBigDecimal("Amount"));

                String pmStr = rs.getString("PaymentMethod").toUpperCase().replace(' ', '_');
                payment.setPaymentMethod(Payment.PaymentMethod.valueOf(pmStr));
                payment.setOrder(orderDAO.getOrderById(rs.getInt("OrderID")));

                payments.add(payment);
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch all payments");
        }
        return payments;
    }

    // Get a payment by its ID
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM Payments_table WHERE PaymentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();

            OrderDAO orderDAO = new OrderDAO(conn);

            if (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("PaymentID"));
                payment.setTransactionId(rs.getString("TransactionID"));
                payment.setPaymentDate(rs.getTimestamp("PaymentDate").toLocalDateTime());
                payment.setAmount(rs.getBigDecimal("Amount"));

                String pmStr = rs.getString("PaymentMethod").toUpperCase().replace(' ', '_');
                payment.setPaymentMethod(Payment.PaymentMethod.valueOf(pmStr));
                payment.setOrder(orderDAO.getOrderById(rs.getInt("OrderID")));

                return payment;
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch payment by ID");
        }
        return null;
    }

    // Update an existing Payment record
    public boolean update(Payment payment) {
        String sql = "UPDATE Payments_table SET OrderID = ?, Amount = ?, PaymentMethod = ?, TransactionID = ?, PaymentDate = ? WHERE PaymentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getOrder().getOrderId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod().name().replace('_', ' '));
            stmt.setString(4, payment.getTransactionId());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setInt(6, payment.getPaymentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update payment");
            return false;
        }
    }

    // Delete a Payment by ID
    public boolean delete(int paymentId) {
        String sql = "DELETE FROM Payments_table WHERE PaymentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete payment");
            return false;
        }
    }
}
