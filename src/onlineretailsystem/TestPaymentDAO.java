package onlineretailsystem;

import onlineretailsystem.ModelClasses.Payment;
import onlineretailsystem.ModelClasses.Payment.PaymentMethod;
import onlineretailsystem.ModelClasses.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class TestPaymentDAO {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();

            PaymentDAO paymentDAO = new PaymentDAO(conn);
            OrderDAO orderDAO = new OrderDAO(conn);

            int validOrderId = 12; // update with actual valid order id

            Order order = orderDAO.getOrderById(validOrderId);
            if (order == null) {
                System.err.println("Order with ID " + validOrderId + " not found. Please check your DB.");
                return;
            }

            Payment payment = new Payment(order, new BigDecimal("120.50"), PaymentMethod.CREDIT_CARD);
            payment.setTransactionId("TXN_TEST_001");
            payment.setPaymentDate(LocalDateTime.now());

            boolean inserted = paymentDAO.insert(payment);
            System.out.println("Insert Payment result: " + inserted);
            System.out.println("Inserted Payment ID: " + payment.getPaymentId());

            Payment fetched = paymentDAO.getPaymentById(payment.getPaymentId());
            System.out.println("\nFetched Payment:");
            System.out.println(fetched);

            List<Payment> allPayments = paymentDAO.getAllPayments();
            System.out.println("\nAll Payments:");
            for (Payment p : allPayments) {
                System.out.println(p);
            }

            payment.setAmount(new BigDecimal("130.75"));
            payment.setPaymentMethod(PaymentMethod.EASYPAISA);
            payment.setTransactionId("TXN_TEST_001_UPDATED");

            boolean updated = paymentDAO.update(payment);
            System.out.println("\nUpdate Payment result: " + updated);

            Payment updatedPayment = paymentDAO.getPaymentById(payment.getPaymentId());
            System.out.println("Updated Payment:");
            System.out.println(updatedPayment);

            boolean deleted = paymentDAO.delete(payment.getPaymentId());
            System.out.println("\nDelete Payment result: " + deleted);

            Payment afterDelete = paymentDAO.getPaymentById(payment.getPaymentId());
            System.out.println("Payment after delete (should be null): " + afterDelete);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(conn);
        }
    }
}
