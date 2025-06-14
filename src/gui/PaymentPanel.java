package gui;

import onlineretailsystem.ModelClasses.Payment;
import onlineretailsystem.PaymentDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentPanel extends JPanel {
    private JTextField orderIdField, amountField, transactionIdField, paymentDateField;
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
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // paymentIdField = createStyledTextField();
        orderIdField = createStyledTextField();
        amountField = createStyledTextField();
        paymentMethodCombo = createStyledComboBox();
        transactionIdField = createStyledTextField();
        paymentDateField = createStyledTextField();
        paymentDateField.setText(LocalDateTime.now().toString());

        int row = 0;

        JLabel formTitle = new JLabel("➕ Add New Payment");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        formPanel.add(formTitle, gbc);

        // Add fields with proper alignment
        // addFormRow(formPanel, "🆔 Payment ID", paymentIdField, row++);
        addFormRow(formPanel, "📦 Order ID", orderIdField, row++);
        addFormRow(formPanel, "💰 Amount", amountField, row++);
        addFormRow(formPanel, "💳 Payment Method", paymentMethodCombo, row++);
        addFormRow(formPanel, "🔁 Transaction ID", transactionIdField, row++);
        addFormRow(formPanel, "🗓️ Payment Date", paymentDateField, row++);

        // Add Create Button
        createPaymentBtn = createPrimaryButton("Create Payment");
        createPaymentBtn.addActionListener(e -> createPayment());

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(createPaymentBtn, gbc);

        return formPanel;
    }

    private void addFormRow(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
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
        button.setForeground(Color.BLACK);
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

// Method to handle payment creation
private void createPayment() {
    try {
        int orderId = Integer.parseInt(orderIdField.getText().trim());
        BigDecimal amount = new BigDecimal(amountField.getText().trim());
        Payment.PaymentMethod method = (Payment.PaymentMethod) paymentMethodCombo.getSelectedItem();
        String transactionId = transactionIdField.getText().trim();
        LocalDateTime paymentDate = LocalDateTime.parse(paymentDateField.getText().trim());

if (amount.compareTo(BigDecimal.ZERO) <= 0) {
    JOptionPane.showMessageDialog(this, "Amount must be greater than 0.",
            "Input Error", JOptionPane.ERROR_MESSAGE);
    return;
}

if (method != Payment.PaymentMethod.CASH_ON_DELIVERY && transactionId.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Transaction ID is required for non-Cash payments.",
            "Input Error", JOptionPane.ERROR_MESSAGE);
    return;
}

        // Create payment object
        Payment payment = new Payment();
        payment.setOrder(new onlineretailsystem.ModelClasses.Order(orderId)); // assumes constructor with orderId
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(paymentDate);

        // Insert into DB
        PaymentDAO paymentDAO = new PaymentDAO();
        boolean success = paymentDAO.insert(payment);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "✅ Payment Created Successfully!\nGenerated ID: " + payment.getPaymentId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this,
                    "❌ Payment creation failed. See console/logs for details.",
                    "Failure", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "❌ Invalid input. Format for Date: yyyy-MM-ddTHH:mm:ss",
                "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    }

    private void clearFields() {
        orderIdField.setText("");
        amountField.setText("");
        transactionIdField.setText("");
        paymentDateField.setText(LocalDateTime.now().toString());
        paymentMethodCombo.setSelectedIndex(0);
    }
}
