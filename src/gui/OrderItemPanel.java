package gui;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class OrderItemPanel extends JPanel {
    private JTextField orderItemIdField, orderIdField, productNameField, quantityField, priceField;
    private JButton createBtn;

    public OrderItemPanel() {
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

        JLabel title = new JLabel("📦 Order Item Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Add items to orders");
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
        orderItemIdField = createStyledTextField();
        orderIdField = createStyledTextField();
        productNameField = createStyledTextField();
        quantityField = createStyledTextField();
        priceField = createStyledTextField();

        // Add form title
        JLabel formTitle = new JLabel("➕ Add New Order Item");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        // Form fields
        addFormField(panel, "🆔 Order Item ID", orderItemIdField, gbc, 1, 0);
        addFormField(panel, "📦 Order ID", orderIdField, gbc, 2, 0);
        addFormField(panel, "📋 Product Name", productNameField, gbc, 3, 0);
        addFormField(panel, "🔢 Quantity", quantityField, gbc, 4, 0);
        addFormField(panel, "💲 Price", priceField, gbc, 5, 0);

        // Create button
        createBtn = createPrimaryButton("Create Order Item");
        createBtn.addActionListener(e -> createOrderItem());
        gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createBtn, gbc);

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

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void createOrderItem() {
        try {
            int orderItemId = Integer.parseInt(orderItemIdField.getText().trim());
            int orderId = Integer.parseInt(orderIdField.getText().trim());
            String productName = productNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            BigDecimal price = new BigDecimal(priceField.getText().trim());

            if (productName.isEmpty() || quantity <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                "✅ Order Item Created:\n" +
                "Order Item ID: " + orderItemId + "\n" +
                "Order ID: " + orderId + "\n" +
                "Product Name: " + productName + "\n" +
                "Quantity: " + quantity + "\n" +
                "Price: " + price,
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "❌ Please enter valid numeric values for IDs, quantity, and price.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        orderItemIdField.setText("");
        orderIdField.setText("");
        productNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }
}