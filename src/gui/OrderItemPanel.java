package gui;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class OrderItemPanel extends JPanel {
    private JTextField orderItemIdField;
    private JTextField orderIdField;
    private JTextField productNameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton createBtn;

    public OrderItemPanel() {
        setLayout(new GridLayout(6, 2, 10, 10));

        orderItemIdField = new JTextField();
        orderIdField = new JTextField();
        productNameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        createBtn = new JButton("Create Order Item");

        add(new JLabel("Order Item ID:"));
        add(orderItemIdField);

        add(new JLabel("Order ID:"));
        add(orderIdField);

        add(new JLabel("Product Name:"));
        add(productNameField);

        add(new JLabel("Quantity:"));
        add(quantityField);

        add(new JLabel("Price:"));
        add(priceField);

        add(new JLabel()); // empty cell
        add(createBtn);

        createBtn.addActionListener(e -> {
            try {
                int orderItemId = Integer.parseInt(orderItemIdField.getText());
                int orderId = Integer.parseInt(orderIdField.getText());
                String productName = productNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                BigDecimal price = new BigDecimal(priceField.getText());

                // Here you should create or fetch Order and Product objects
                // For now, let's just print the values
                JOptionPane.showMessageDialog(this,
                        "OrderItem Created:\n" +
                                "OrderItemId: " + orderItemId + "\n" +
                                "OrderId: " + orderId + "\n" +
                                "ProductName: " + productName + "\n" +
                                "Quantity: " + quantity + "\n" +
                                "Price: " + price);

                // You can implement actual object creation like:
                // Product product = new Product(productName, ...);
                // Order order = findOrderById(orderId);
                // OrderItem orderItem = new OrderItem(orderItemId, order, product, quantity, price);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for IDs, quantity, and price.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
