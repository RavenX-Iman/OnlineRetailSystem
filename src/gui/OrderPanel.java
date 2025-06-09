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


public class OrderPanel extends JPanel {

    private JTextField customerNameField;
    private JTextField totalAmountField;
    private JComboBox<OrderStatus> statusComboBox;
    private JTextArea orderItemsArea;
    private JTextArea outputArea;

    public OrderPanel() {
        setLayout(new BorderLayout());

        // Top Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        customerNameField = new JTextField();
        totalAmountField = new JTextField();
        statusComboBox = new JComboBox<>(OrderStatus.values());
        orderItemsArea = new JTextArea(3, 20);

        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(customerNameField);
        formPanel.add(new JLabel("Total Amount:"));
        formPanel.add(totalAmountField);
        formPanel.add(new JLabel("Order Status:"));
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel("Order Items:"));
        formPanel.add(new JScrollPane(orderItemsArea));

        JButton createOrderBtn = new JButton("Create Order");
        formPanel.add(new JLabel());
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
            BigDecimal total = new BigDecimal(totalAmountField.getText());
            OrderStatus status = (OrderStatus) statusComboBox.getSelectedItem();
            String itemsText = orderItemsArea.getText();

            // Fake customer just for display (You can load actual Customer from DB)
            Customer customer = new Customer();
            customer.setFirstName(customerName);

            // Fake order items list
            List<OrderItem> orderItems = new ArrayList<>();
            String[] itemLines = itemsText.split("\n");
            for (String line : itemLines) {
                OrderItem item = new OrderItem(); // Add real parsing later
                item.setProductName(line);
                item.setQuantity(1); // Example
                orderItems.add(item);
            }

            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderDate(LocalDateTime.now());
            order.setTotalAmount(total);
            order.setStatus(status);
            order.setOrderItems(orderItems);

            outputArea.setText("Order Created Successfully:\n" + order.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
