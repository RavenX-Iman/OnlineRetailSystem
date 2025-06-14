package gui;

import onlineretailsystem.CustomerDAO;
import onlineretailsystem.DBConnection;
import onlineretailsystem.ModelClasses;
import onlineretailsystem.ModelClasses.Customer;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.OrderDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {

    private JTextField customerNameField, totalAmountField;
    private JComboBox<Order.OrderStatus> statusComboBox;
    private JTextArea orderItemsArea, outputArea;

    public OrderPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // ---------- HEADER PANEL ----------
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("🛒 Order Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Create and manage customer orders");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        header.add(subtitle, BorderLayout.SOUTH);

        return header;
    }

    // ---------- MAIN CONTENT AREA ----------
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(ModernColors.BACKGROUND);
        main.add(createFormPanel(), BorderLayout.NORTH);
        main.add(createOutputPanel(), BorderLayout.CENTER);
        return main;
    }

    // ---------- ORDER FORM PANEL ----------
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        int row = 0;

        // Form Title
        JLabel formTitle = new JLabel("➕ Create New Order");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        gbc.gridwidth = 1;

        // Customer Name
        addFormField(panel, "👤 Customer Name", customerNameField = createStyledTextField(), gbc, row++);

        // Total Amount
        addFormField(panel, "💰 Total Amount", totalAmountField = createStyledTextField(), gbc, row++);

        // Order Status
        addFormField(panel, "📋 Order Status", statusComboBox = createStyledComboBox(), gbc, row++);

        // Order Items Label
        JLabel itemsLabel = new JLabel("📦 Order Items (one per line)");
        itemsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemsLabel.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(itemsLabel, gbc);

        // Order Items Area
        orderItemsArea = createStyledTextArea();
        JScrollPane scrollPane = new JScrollPane(orderItemsArea);
        scrollPane.setPreferredSize(new Dimension(300, 70));
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        gbc.gridy = row++;
        panel.add(scrollPane, gbc);

        // Create Button
        JButton createButton = createPrimaryButton("Create Order");
        createButton.addActionListener(e -> createOrder());
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    // ---------- ORDER OUTPUT PANEL ----------
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // JLabel outputTitle = new JLabel("📋 Order Details");
        // outputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // outputTitle.setForeground(ModernColors.TEXT_PRIMARY);
        // outputTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        // panel.add(outputTitle, BorderLayout.NORTH);

        outputArea = new JTextArea(6, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        outputArea.setBackground(ModernColors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.getViewport().setBackground(ModernColors.BACKGROUND);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ---------- UTILITY METHODS ----------
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
    }

    private JComboBox<Order.OrderStatus> createStyledComboBox() {
        JComboBox<Order.OrderStatus> combo = new JComboBox<>(Order.OrderStatus.values());
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER, 1));
        return combo;
    }

    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(3, 20);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return area;
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

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col) {
        addFormField(panel, label, field, gbc, row, col, 1);
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col, int width) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col;
        gbc.gridy = row + 1;
        gbc.gridwidth = width;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    // ---------- ORDER CREATION LOGIC ----------
private void createOrder() {
    String customerName = customerNameField.getText().trim();
    String totalAmountStr = totalAmountField.getText().trim();
    String status = statusComboBox.getSelectedItem().toString();
  String itemsText = orderItemsArea.getText().trim();


    if (customerName.isEmpty() || totalAmountStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all required fields.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        Connection conn = DBConnection.getConnection(); // Handle SQLException
CustomerDAO customerDAO = CustomerDAO.getInstance(conn); // ✅ Singleton usage

        // CustomerDAO customerDAO = new CustomerDAO();
        ModelClasses.Customer customer = customerDAO.getCustomerByName(customerName);

        if (customer == null) {
            JOptionPane.showMessageDialog(this, "Customer not found in database.",
                    "Customer Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal totalAmount = new BigDecimal(totalAmountStr);

        ModelClasses.Order order = new ModelClasses.Order(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus(ModelClasses.Order.OrderStatus.valueOf(status));

        // Optional: Parse order items if needed
        List<ModelClasses.OrderItem> items = new ArrayList<>();
        for (String item : itemsText.split("\n")) {
            item = item.trim();
            if (!item.isEmpty()) {
                ModelClasses.Product product = new ModelClasses.Product();
                product.setProductName(item);

                ModelClasses.OrderItem orderItem = new ModelClasses.OrderItem();
                orderItem.setProduct(product);
                orderItem.setQuantity(1);  // Default quantity
                items.add(orderItem);
            }
        }

       order.setOrderItems(items);

        // Insert order into database
        OrderDAO orderDAO = new OrderDAO();
        int result = orderDAO.insertOrder(order);

        if (result > 0) {
            outputArea.setText("✅ Order Created Successfully:\n\n" + order.toString());
            JOptionPane.showMessageDialog(this, "Order created successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();  // Optional: Reset input fields after success
        } else {
            JOptionPane.showMessageDialog(this, "Failed to insert order into database.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid total amount.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void clearFields() {
        customerNameField.setText("");
        totalAmountField.setText("");
        orderItemsArea.setText("");
        statusComboBox.setSelectedIndex(0);
    }
}
