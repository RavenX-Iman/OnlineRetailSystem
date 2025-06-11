package gui;

import onlineretailsystem.InventoryManager;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.ModelClasses.Product;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductPanel extends JPanel {
    private JTextField nameField, priceField, stockField, createdByField;
    private JComboBox<String> categoryBox;
    private JButton createBtn;

    public ProductPanel() {
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

        JLabel title = new JLabel("📦 Product Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Add and manage inventory products");
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
        nameField = createStyledTextField();
        priceField = createStyledTextField();
        stockField = createStyledTextField();
        createdByField = createStyledTextField();
        categoryBox = createStyledComboBox();

        // Add form title
        JLabel formTitle = new JLabel("➕ Add New Product");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        // Form fields
        addFormField(panel, "📦 Product Name", nameField, gbc, 1, 0);
        addFormField(panel, "🗂️ Category", categoryBox, gbc, 2, 0);
        addFormField(panel, "💵 Price", priceField, gbc, 3, 0);
        addFormField(panel, "📦 Stock", stockField, gbc, 4, 0);
        addFormField(panel, "🧑 Created By", createdByField, gbc, 5, 0);

        // Create button
        createBtn = createPrimaryButton("Create Product");
        createBtn.addActionListener(e -> handleCreateProduct());
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

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>(new String[]{"Electronics", "Books", "Clothing", "Groceries"});
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER, 1));
        combo.setBackground(Color.WHITE);
        return combo;
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
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void handleCreateProduct() {
        try {
            String name = nameField.getText().trim();
            String categoryName = (String) categoryBox.getSelectedItem();
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String createdBy = createdByField.getText().trim();
            LocalDateTime now = LocalDateTime.now();

            if (name.isEmpty() || price.compareTo(BigDecimal.ZERO) <= 0 || stock < 0 || createdBy.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields correctly.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = new Category(0, categoryName, "");
            Product product = new Product(0, name, category, price, stock, now, createdBy, now);
            InventoryManager.getInstance().updateStock(product, stock);

            JOptionPane.showMessageDialog(this, "✅ Product Created:\n" + product.toString(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), 
                "Input Error", JOptionPane.ERROR_MESSAGE);
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