package gui;

import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.InventoryTransaction.TransactionType;

import javax.swing.*;
        import java.awt.*;
        import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryTransactionPanel extends JPanel {

    public InventoryTransactionPanel() {
        setLayout(new GridLayout(6, 2, 10, 10));

        // Input fields
        JTextField productNameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField reasonField = new JTextField();

        JComboBox<TransactionType> typeComboBox = new JComboBox<>(TransactionType.values());
        JButton addTransactionBtn = new JButton("Add Inventory Transaction");

        // Add components
        add(new JLabel("Product Name:"));
        add(productNameField);

        add(new JLabel("Product Price:"));
        add(priceField);

        add(new JLabel("Quantity:"));
        add(quantityField);

        add(new JLabel("Transaction Type:"));
        add(typeComboBox);

        add(new JLabel("Reason:"));
        add(reasonField);

        add(new JLabel()); // empty cell
        add(addTransactionBtn);

        // Button action
        addTransactionBtn.addActionListener(e -> {
            try {
                String productName = productNameField.getText();
                BigDecimal price = new BigDecimal(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                TransactionType type = (TransactionType) typeComboBox.getSelectedItem();
                String reason = reasonField.getText();

                Product product = new Product(productName, null, price, quantity); // Pass null for category if not used
                InventoryTransaction transaction = new InventoryTransaction();
                transaction.setProduct(product);
                transaction.setTransactionType(type);
                transaction.setQuantity(quantity);
                transaction.setReason(reason);
                transaction.setCreatedAt(LocalDateTime.now());

                JOptionPane.showMessageDialog(this, "Inventory Transaction Created:\n" + transaction);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}

