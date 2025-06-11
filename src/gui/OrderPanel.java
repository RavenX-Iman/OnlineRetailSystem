package gui;

import onlineretailsystem.ModelClasses.Customer;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.OrderItem;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {
    private JTextField customerNameField, totalAmountField;
    private JComboBox<Order.OrderStatus> statusComboBox;
    private JTextArea orderItemsArea;
    private JTextArea outputArea;

    public OrderPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

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

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(ModernColors.BACKGROUND);

        main.add(createFormPanel(), BorderLayout.NORTH);
        main.add(createOutputPanel(), BorderLayout.CENTER);

        return main;
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
        customerNameField = createStyledTextField();
        totalAmountField = createStyledTextField();
        statusComboBox = createStyledComboBox();
        orderItemsArea = createStyledTextArea();

        // Add form title
        JLabel formTitle = new JLabel("➕ Create New Order");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        // Form fields
        addFormField(panel, "👤 Customer Name", customerNameField, gbc, 1, 0);
        addFormField(panel, "💰 Total Amount", totalAmountField, gbc, 2, 0);
        addFormField(panel, "📋 Order Status", statusComboBox, gbc, 3, 0);
        addFormField(panel, "📦 Order Items (one per line)", orderItemsArea, gbc, 4, 0, 2);

        // Create button
        JButton createButton = createPrimaryButton("Create Order");
        createButton.addActionListener(e -> createOrder());
        gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel outputTitle = new JLabel("📋 Order Details");
        outputTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        outputTitle.setForeground(ModernColors.TEXT_PRIMARY);
        outputTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(outputTitle, BorderLayout.NORTH);

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
        combo.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER, 1));
        combo.setBackground(Color.WHITE);
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
        addFormField(panel, label, field, gbc, row, col, 1);
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col, int width) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = width; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void createOrder() {
        try {
            String customerName = customerNameField.getText().trim();
            BigDecimal total = new BigDecimal(totalAmountField.getText().trim());
            Order.OrderStatus status = (Order.OrderStatus) statusComboBox.getSelectedItem();
            String[] items = orderItemsArea.getText().split("\n");

            if (customerName.isEmpty() || total.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields correctly.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = new Customer();
            customer.setFirstName(customerName);

            List<OrderItem> orderItems = new ArrayList<>();
            for (String item : items) {
                if (!item.isBlank()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.getProduct().setProductName(item.trim());
                    orderItem.setQuantity(1);
                    orderItems.add(orderItem);
                }
            }

            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(total);
            order.setStatus(status);
            order.setOrderItems(orderItems);

            outputArea.setText("✅ Order Created:\n\n" + order.toString());
            clearFields();
            JOptionPane.showMessageDialog(this, "Order created successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        customerNameField.setText("");
        totalAmountField.setText("");
        orderItemsArea.setText("");
        statusComboBox.setSelectedIndex(0);
    }
}