package gui;

import onlineretailsystem.ModelClasses.Payment;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;



public class PaymentPanel extends JPanel {
    private JTextField paymentIdField;
    private JTextField orderIdField;
    private JTextField amountField;
    private JComboBox<Payment.PaymentMethod> paymentMethodCombo;
    private JTextField transactionIdField;
    private JTextField paymentDateField;  // Format: yyyy-MM-ddTHH:mm:ss

    private JButton createPaymentBtn;

    public PaymentPanel() {
        setLayout(new GridLayout(7, 2, 10, 10));

        paymentIdField = new JTextField();
        orderIdField = new JTextField();
        amountField = new JTextField();
        paymentMethodCombo = new JComboBox<>(Payment.PaymentMethod.values());
        transactionIdField = new JTextField();
        paymentDateField = new JTextField(LocalDateTime.now().toString());

        createPaymentBtn = new JButton("Create Payment");

        add(new JLabel("Payment ID:"));
        add(paymentIdField);

        add(new JLabel("Order ID:"));
        add(orderIdField);

        add(new JLabel("Amount:"));
        add(amountField);

        add(new JLabel("Payment Method:"));
        add(paymentMethodCombo);

        add(new JLabel("Transaction ID:"));
        add(transactionIdField);

        add(new JLabel("Payment Date (yyyy-MM-ddTHH:mm:ss):"));
        add(paymentDateField);

        add(new JLabel()); // empty cell
        add(createPaymentBtn);

        createPaymentBtn.addActionListener(e -> {
            try {
                int paymentId = Integer.parseInt(paymentIdField.getText());
                int orderId = Integer.parseInt(orderIdField.getText());
                BigDecimal amount = new BigDecimal(amountField.getText());
                Payment.PaymentMethod method = (Payment.PaymentMethod) paymentMethodCombo.getSelectedItem();
                String transactionId = transactionIdField.getText();
                LocalDateTime paymentDate = LocalDateTime.parse(paymentDateField.getText());

                // TODO: Lookup or create Order object using orderId
                // Order order = findOrderById(orderId);

                // For demo, just show a message
                JOptionPane.showMessageDialog(this,
                        "Payment Created:\n" +
                                "PaymentId: " + paymentId + "\n" +
                                "OrderId: " + orderId + "\n" +
                                "Amount: " + amount + "\n" +
                                "Method: " + method + "\n" +
                                "Transaction ID: " + transactionId + "\n" +
                                "Payment Date: " + paymentDate);

                // Actual object creation:
                // Payment payment = new Payment(paymentId, order, amount, method, transactionId, paymentDate);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid inputs.\nDate format: yyyy-MM-ddTHH:mm:ss", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

