package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import onlineretailsystem.DBConnection;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ProductDAO;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.OrderItemDAO;
import java.math.BigDecimal;

public class OrderItemPanel extends JPanel {
    private JTextField orderItemIdField, orderIdField, productNameField, quantityField, priceField;
    private JButton createBtn;

    public OrderItemPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize fields
        orderItemIdField = createStyledTextField();
        orderIdField = createStyledTextField();
        productNameField = createStyledTextField();
        quantityField = createStyledTextField();
        priceField = createStyledTextField();

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
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel formTitle = new JLabel("➕ Add New Order Item");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        panel.add(formTitle, gbc);

        gbc.insets = new Insets(12, 10, 4, 10);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        addFormField(panel, "🆔 Order Item ID", orderItemIdField, gbc);

        gbc.gridy++;
        addFormField(panel, "📦 Order ID", orderIdField, gbc);

        gbc.gridy++;
        addFormField(panel, "📋 Product Name", productNameField, gbc);

        gbc.gridy++;
        addFormField(panel, "🔢 Quantity", quantityField, gbc);

        gbc.gridy++;
        addFormField(panel, "💲 Price", priceField, gbc);

        // Button
        createBtn = createPrimaryButton("Create Order Item");
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createBtn, gbc);

        createBtn.addActionListener(e -> createOrderItem());

        return panel;
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(220, 30));
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


        private void createOrderItem() {
    try {
        int orderId = Integer.parseInt(orderIdField.getText().trim());
        String productName = productNameField.getText().trim();
        int quantity = Integer.parseInt(quantityField.getText().trim());
        BigDecimal price = new BigDecimal(priceField.getText().trim());

        if (productName.isEmpty() || quantity <= 0 || price.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = DBConnection.getConnection();

        // Fetch product from DB
        ProductDAO productDAO = new ProductDAO(conn);
        Product product = productDAO.getProductByName(productName);

        if (product == null) {
            JOptionPane.showMessageDialog(this, "❌ Product not found: " + productName,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = new Order();
        order.setOrderId(orderId); // Assume order already exists

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(price);

        OrderItemDAO itemDAO = new OrderItemDAO(conn);
        itemDAO.insertOrderItem(item);

        JOptionPane.showMessageDialog(this,
                "✅ Order Item Created Successfully!\nID: " + item.getOrderItemId(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
                "❌ Please enter valid numeric values for Order ID, quantity, and price.",
                "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "❌ Error creating order item: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
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
