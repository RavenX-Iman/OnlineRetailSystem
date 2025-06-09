package gui;

import onlineretailsystem.InventoryManager;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.ModelClasses.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ProductPanel extends JPanel {

    private JTextField nameField, priceField, stockField, createdByField;
    private JComboBox<String> categoryBox;
    private JButton createBtn;

    public ProductPanel() {
        setLayout(new GridLayout(9, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Add New Product"));

        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();
        createdByField = new JTextField();

        // Simulating Category choices (replace with actual DB categories)
        categoryBox = new JComboBox<>(new String[]{"Electronics", "Books", "Clothing", "Groceries"});

        createBtn = new JButton("Create Product");

        add(new JLabel("Product Name:"));
        add(nameField);
        add(new JLabel("Category:"));
        add(categoryBox);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Stock:"));
        add(stockField);
        add(new JLabel("Created By:"));
        add(createdByField);
        add(new JLabel("")); // empty cell
        add(createBtn);

        createBtn.addActionListener(this::handleCreateProduct);
    }

    private void handleCreateProduct(ActionEvent e) {
        try {
            String name = nameField.getText();
            String categoryName = (String) categoryBox.getSelectedItem();
            BigDecimal price = new BigDecimal(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String createdBy = createdByField.getText();
            LocalDateTime now = LocalDateTime.now();

            Category category = new Category(0, categoryName, ""); // Replace with proper ID from DB if needed

            Product product = new Product(0, name, category, price, stock, now, createdBy, now);


            // Simulate saving to DB or inventory
            InventoryManager.getInstance().updateStock(product, stock);

            JOptionPane.showMessageDialog(this, "Product Created:\n" + product.toString());

            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        createdByField.setText("");
        categoryBox.setSelectedIndex(0);
    }
}
