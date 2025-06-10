package Service;

import onlineretailsystem.PaymentDAO;
import onlineretailsystem.ModelClasses.Payment;
import onlineretailsystem.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

public class PaymentService {
    private final PaymentDAO dao;
    private final Connection connection;

    public PaymentService() {
        try {
            this.connection = DBConnection.getConnection();
            this.dao = new PaymentDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public List<Payment> getAllPayments() {
        return dao.getAllPayments();
    }

    public boolean addPayment(Payment payment) {
        if (payment == null || payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return dao.insert(payment);
    }

    public Payment getPaymentById(int paymentId) {
        return dao.getPaymentById(paymentId);
    }

    public boolean deletePayment(int paymentId) {
        return dao.delete(paymentId);
    }

    public void updatePayment(Payment payment) {
        if (payment == null || payment.getPaymentId() <= 0) {
            throw new IllegalArgumentException("Invalid payment data");
        }
        dao.update(payment);
    }   

    public void close() {
        DBConnection.closeConnection(connection);
    }
}