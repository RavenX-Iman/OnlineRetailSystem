package gui;

import onlineretailsystem.InventoryManager;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.ModelClasses.Product;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import onlineretailsystem.ProductDAO;
import onlineretailsystem.CategoryDAO;
import onlineretailsystem.DBConnection;
import java.sql.Connection;

public class ProductPanel extends JPanel {

    // Modern Color Palette
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);      // Modern blue
    private static final Color SECONDARY_BLUE = new Color(59, 130, 246);   // Lighter blue
    private static final Color BACKGROUND_MAIN = new Color(248, 250, 252);  // Very light gray
    private static final Color BACKGROUND_CARD = Color.WHITE;               // Pure white for cards
    private static final Color TEXT_PRIMARY = new Color(30, 41, 59);        // Dark slate
    private static final Color TEXT_SECONDARY = new Color(100, 116, 139);   // Medium slate
    private static final Color BORDER_COLOR = new Color(226, 232, 240);     // Light border
    private static final Color FOCUS_COLOR = new Color(59, 130, 246);       // Focus blue
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);      // Success green
    private static final Color HOVER_COLOR = new Color(29, 78, 216);        // Darker blue for hover

    private JTextField nameField, priceField, stockField, createdByField;
    private JComboBox<String> categoryBox;
    private JButton createBtn, clearBtn, cancelBtn;

    public ProductPanel() {
        initializePanel();
        createComponents();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_MAIN);
        setBorder(new EmptyBorder(25, 25, 25, 25));
    }

    private void createComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {

    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(ModernColors.PRIMARY);
    header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

    // Main title
    JLabel title = new JLabel("📦 Product Management");
    title.setFont(new Font("Segoe UI", Font.BOLD, 24));
    title.setForeground(Color.WHITE);
    header.add(title, BorderLayout.WEST);

    // Subtitle
    JLabel subtitle = new JLabel("Add and manage inventory products efficiently");
    subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    subtitle.setForeground(new Color(200, 200, 255));
    header.add(subtitle, BorderLayout.SOUTH);

    return header;
}




    private JPanel createMainFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_MAIN);

        // Initialize fields first
        nameField = createStyledTextField("Product Name");
        categoryBox = createStyledComboBox();
        priceField = createStyledTextField("0.00");
        stockField = createStyledTextField("0");
        createdByField = createStyledTextField("Created By");

        // Create card-style form panel
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(BACKGROUND_CARD);
        formCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Form title
        JLabel formTitle = new JLabel("➕ Add New Product");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(PRIMARY_BLUE);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
        formCard.add(formTitle);

        // Add form sections
        formCard.add(createProductInfoSection());

        mainPanel.add(formCard, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createProductInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND_CARD);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // First row: Product Name and Category
        JPanel nameRow = new JPanel(new GridLayout(1, 2, 20, 0));
        nameRow.setBackground(BACKGROUND_CARD);
        nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameRow.getPreferredSize().height + 60));

        nameRow.add(createFieldPanel("Product Name *", nameField));
        nameRow.add(createFieldPanel("Category *", categoryBox));

        section.add(nameRow);
        section.add(Box.createVerticalStrut(20));

        // Second row: Price and Stock
        JPanel priceStockRow = new JPanel(new GridLayout(1, 2, 20, 0));
        priceStockRow.setBackground(BACKGROUND_CARD);
        priceStockRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        priceStockRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, priceStockRow.getPreferredSize().height + 60));

        priceStockRow.add(createFieldPanel("Price *", priceField));
        priceStockRow.add(createFieldPanel("Stock Quantity *", stockField));
        
        section.add(priceStockRow);
        section.add(Box.createVerticalStrut(20));

        // Third row: Created By (full width)
        JPanel createdByPanel = createFieldPanel("Created By *", createdByField);
        createdByPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        createdByPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, createdByPanel.getPreferredSize().height + 60)
        );
        //createdByPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, createdByPanel.getPreferredSize().height));
        section.add(createdByPanel);

        return section;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(BACKGROUND_MAIN);
        buttonPanel.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Clear button
        clearBtn = createStyledButton("Clear", BORDER_COLOR, TEXT_SECONDARY);
        clearBtn.addActionListener(e -> clearFields());

        // Cancel button
        cancelBtn = createStyledButton("Cancel", BORDER_COLOR, TEXT_SECONDARY);

        // Create button (primary)
        createBtn = createPrimaryButton("✅ Create Product");
        createBtn.addActionListener(e -> handleCreateProduct());

        // Add buttons with spacing
        buttonPanel.add(clearBtn);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(createBtn);

        return buttonPanel;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_CARD);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (field instanceof JTextField) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        } else if (field instanceof JComboBox) {
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        }

        panel.add(label);
        panel.add(field);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 16, 12, 16)
        ));
        field.setPreferredSize(new Dimension(200, 44));

        // Add focus effects
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(FOCUS_COLOR, 2, true),
                        new EmptyBorder(11, 15, 11, 15)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(12, 16, 12, 16)
                ));
            }
        });

        return field;
    }

    private JComboBox<String> createStyledComboBox() {
        String[] categories = {"Electronics", "Books", "Clothing", "Groceries", "Home & Garden", "Sports", "Beauty"};
        JComboBox<String> combo = new JComboBox<>(categories);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));
        combo.setPreferredSize(new Dimension(200, 44));
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Custom renderer for better styling
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setBorder(new EmptyBorder(10, 12, 10, 12));

                if (isSelected) {
                    setBackground(SECONDARY_BLUE);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(TEXT_PRIMARY);
                }
                return this;
            }
        });

        return combo;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(PRIMARY_BLUE);
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 44));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_BLUE);
            }
        });

        return button;
    }

    private JButton createStyledButton(String text, Color borderColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(textColor);
        button.setBackground(Color.WHITE);
        button.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(11, 23, 11, 23)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 44));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(249, 250, 251));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }
// Then modify the handleCreateProduct() method:
private void handleCreateProduct() {
    try {
        String name = nameField.getText().trim();
        String categoryName = (String) categoryBox.getSelectedItem();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String createdBy = createdByField.getText().trim();

        // Validation
        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || createdBy.isEmpty()) {
            showErrorDialog("Please fill in all required fields (marked with *)");
            return;
        }

        BigDecimal price;
        int stock;

        try {
            price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                showErrorDialog("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid price (e.g., 19.99)");
            return;
        }

        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) {
                showErrorDialog("Stock quantity cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Please enter a valid stock quantity (whole number)");
            return;
        }

        // Get database connection
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            showErrorDialog("Database connection failed");
            return;
        }

        // Get category by name
        CategoryDAO categoryDAO = new CategoryDAO(conn);
        Category category = null;
        java.util.List<Category> categories = categoryDAO.getAllCategories();
        
        for (Category cat : categories) {
            if (cat.getCategoryName().equalsIgnoreCase(categoryName)) {
                category = cat;
                break;
            }
        }
        
        if (category == null) {
            showErrorDialog("Category '" + categoryName + "' not found in database. Please add it first or select an existing category.");
            return;
        }

        // Create product
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product(0, name, category, price, stock, now, createdBy, now);

        // Save to database
        ProductDAO productDAO = new ProductDAO(conn);
        if (productDAO.insertProduct(product)) {
            // Update inventory manager after successful database insert
            InventoryManager.getInstance().updateStock(product, stock);

            // Success message
            showSuccessDialog("Product created successfully!\n\n" + 
                    "Name: " + name + "\n" + 
                    "Category: " + categoryName + "\n" +
                    "Price: $" + price + "\n" + 
                    "Stock: " + stock + " units");

            clearFields();
        } else {
            showErrorDialog("Failed to save product to database");
        }

    } catch (Exception ex) {
        showErrorDialog("Error creating product: " + ex.getMessage());
    }
}
    

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        createdByField.setText("");
        categoryBox.setSelectedIndex(0);
        nameField.requestFocus();
    }

    // Getter methods for accessing field values
    public String getProductName() { return nameField.getText().trim(); }
    public String getSelectedCategory() { return (String) categoryBox.getSelectedItem(); }
    public String getPrice() { return priceField.getText().trim(); }
    public String getStock() { return stockField.getText().trim(); }
    public String getCreatedBy() { return createdByField.getText().trim(); }
}