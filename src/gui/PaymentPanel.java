package gui;

import onlineretailsystem.ModelClasses.Payment;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentPanel extends JPanel {
    private JTextField paymentIdField, orderIdField, amountField, transactionIdField, paymentDateField;
    private JComboBox<Payment.PaymentMethod> paymentMethodCombo;
    private JButton createPaymentBtn;

    public PaymentPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("💳 Payment Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Process and track payments");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        header.add(subtitle, BorderLayout.SOUTH);

        return header;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize fields
        paymentIdField = createStyledTextField();
        orderIdField = createStyledTextField();
        amountField = createStyledTextField();
        paymentMethodCombo = createStyledComboBox();
        transactionIdField = createStyledTextField();
        paymentDateField = createStyledTextField();
        paymentDateField.setText(LocalDateTime.now().toString());

        // Add form title
        JLabel formTitle = new JLabel("➕ Add New Payment");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        // Form fields
        addFormField(panel, "🆔 Payment ID", paymentIdField, gbc, 1, 0);
        addFormField(panel, "📦 Order ID", orderIdField, gbc, 2, 0);
        addFormField(panel, "💰 Amount", amountField, gbc, 3, 0);
        addFormField(panel, "💳 Payment Method", paymentMethodCombo, gbc, 4, 0);
        addFormField(panel, "🔁 Transaction ID", transactionIdField, gbc, 5, 0);
        addFormField(panel, "🗓️ Payment Date", paymentDateField, gbc, 6, 0);

        // Create button
        createPaymentBtn = createPrimaryButton("Create Payment");
        createPaymentBtn.addActionListener(e -> createPayment());
        gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createPaymentBtn, gbc);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }

    private JComboBox<Payment.PaymentMethod> createStyledComboBox() {
        JComboBox<Payment.PaymentMethod> combo = new JComboBox<>(Payment.PaymentMethod.values());
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER, 1));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ModernColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY);
            }
        });
        return button;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void createPayment() {
        try {
            int paymentId = Integer.parseInt(paymentIdField.getText().trim());
            int orderId = Integer.parseInt(orderIdField.getText().trim());
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            Payment.PaymentMethod method = (Payment.PaymentMethod) paymentMethodCombo.getSelectedItem();
            String transactionId = transactionIdField.getText().trim();
            LocalDateTime paymentDate = LocalDateTime.parse(paymentDateField.getText().trim());

            if (amount.compareTo(BigDecimal.ZERO) <= 0 || transactionId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields correctly.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                "✅ Payment Created:\n" +
                "Payment ID: " + paymentId + "\n" +
                "Order ID: " + orderId + "\n" +
                "Amount: " + amount + "\n" +
                "Method: " + method + "\n" +
                "Transaction ID: " + transactionId + "\n" +
                "Payment Date: " + paymentDate,
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Please enter valid inputs.\nDate format: yyyy-MM-ddTHH:mm:ss",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        paymentIdField.setText("");
        orderIdField.setText("");
        amountField.setText("");
        transactionIdField.setText("");
        paymentDateField.setText(LocalDateTime.now().toString());
        paymentMethodCombo.setSelectedIndex(0);
    }
}