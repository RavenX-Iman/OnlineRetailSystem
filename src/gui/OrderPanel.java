package gui;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import onlineretailsystem.ModelClasses.Customer;
import onlineretailsystem.ModelClasses.Order;
import onlineretailsystem.ModelClasses.Order.OrderStatus;
import onlineretailsystem.ModelClasses.OrderItem;
import onlineretailsystem.ModelClasses.Product;

public class OrderPanel extends JPanel {

    private JTextField customerNameField;
    private JComboBox<OrderStatus> statusComboBox;
    private JTextArea orderItemsArea;
    private JTextArea outputArea;

    public OrderPanel() {
        setLayout(new BorderLayout());

        // Top Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        customerNameField = new JTextField();
        statusComboBox = new JComboBox<>(OrderStatus.values());
        orderItemsArea = new JTextArea(3, 20);

        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(customerNameField);
        formPanel.add(new JLabel("Order Status:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Order Items (name:qty):"));
        formPanel.add(new JScrollPane(orderItemsArea));

        JButton createOrderBtn = new JButton("Create Order");
        formPanel.add(new JLabel()); // empty label for spacing
        formPanel.add(createOrderBtn);

        add(formPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button Action
        createOrderBtn.addActionListener(e -> createOrder());
    }

    private void createOrder() {
        try {
            String customerName = customerNameField.getText();
            OrderStatus status = (OrderStatus) statusComboBox.getSelectedItem();
            String itemsText = orderItemsArea.getText();

            Customer customer = new Customer();
            customer.setFirstName(customerName);

            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            String[] itemLines = itemsText.split("\n");
            for (String line : itemLines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(":");
                String productName = parts[0].trim();
                int quantity = (parts.length > 1) ? Integer.parseInt(parts[1].trim()) : 1;

                Product p = new Product();
                p.setProductName(productName);
                p.setPrice(BigDecimal.valueOf(10)); // dummy price, replace later

                OrderItem item = new OrderItem();
                item.setProduct(p);
                item.setQuantity(quantity);

                BigDecimal itemTotal = p.getPrice().multiply(BigDecimal.valueOf(quantity));
                total = total.add(itemTotal);

                orderItems.add(item);
            }

            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(status);
            order.setOrderItems(orderItems);
            order.setTotalAmount(total);

            outputArea.setText("Order Created Successfully:\n" + order.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
