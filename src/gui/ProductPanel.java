package gui;

import onlineretailsystem.CategoryDAO;
import onlineretailsystem.DBConnection;
import onlineretailsystem.InventoryManager;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ProductDAO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductPanel extends JPanel {
    public ProductPanel() {
    this(createDefaultProductDAO());
}
private static ProductDAO createDefaultProductDAO() {
    try {
        Connection conn = DBConnection.getConnection();
        return new ProductDAO(conn);
    } catch (SQLException e) {
        e.printStackTrace();
        return null; // Or throw RuntimeException if null isn't acceptable
    }
}
        private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO; // moved up
    public ProductPanel(ProductDAO productDAO) {
    this.productDAO = productDAO;
    this.categoryDAO = new CategoryDAO(); // moved up

    setLayout(new BorderLayout());
    setBackground(new Color(248, 250, 252));
    setBorder(new EmptyBorder(25, 25, 25, 25));

    createComponents();

    JComboBox<Category> categoryComboBox = createCategoryComboBox();
    add(categoryComboBox, BorderLayout.NORTH); // add properly

    Category selected = (Category) categoryComboBox.getSelectedItem();
    if (selected != null) {
        int selectedCategoryId = selected.getCategoryId();
        System.out.println("Selected category ID: " + selectedCategoryId);
    }

    initializePanel();
}

     
    // createComponents() stays the same


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
    private JComboBox<Category> categoryBox;
private JButton createBtn, clearBtn, cancelBtn;



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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_MAIN);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Main title
        JLabel titleLabel = new JLabel("📦 Product Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Add and manage inventory products efficiently");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setBorder(new EmptyBorder(20, 0, 0, 0));

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);
        headerPanel.add(separator);

        return headerPanel;
    }

private JPanel createMainFormPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(BACKGROUND_MAIN);

    // Initialize fields once here
    nameField = createStyledTextField("Product Name");
    categoryBox = createCategoryComboBox();
    priceField = createStyledTextField("0.00");
    stockField = createStyledTextField("0");
    createdByField = createStyledTextField("Created By");

    // Create card-style form panel
    JPanel formCard = new JPanel();
    formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
    formCard.setBackground(BACKGROUND_CARD);
    formCard.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(0, 0, 0, 0)
    ));

    // Form title
    JLabel formTitle = new JLabel("➕ Add New Product");
    formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    formTitle.setForeground(PRIMARY_BLUE);
    formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    formTitle.setBorder(new EmptyBorder(0, 0, 25, 0));
    formCard.add(formTitle);

    // Add form sections, using already initialized fields
    formCard.add(createProductInfoSection());

    mainPanel.add(formCard, BorderLayout.CENTER);
    return mainPanel;
}

private JPanel createProductInfoSection() {
    JPanel section = new JPanel();
    section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
    section.setBackground(BACKGROUND_CARD);
    section.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Do NOT reinitialize fields here — use the ones initialized in createMainFormPanel()

    // First row: Product Name and Category
    JPanel nameRow = new JPanel(new GridLayout(1, 2, 20, 0));
    nameRow.setBackground(BACKGROUND_CARD);
    nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    nameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameRow.getPreferredSize().height));

    nameRow.add(createFieldPanel("Product Name ", nameField));
    nameRow.add(createFieldPanel("Category", categoryBox));

    section.add(nameRow);
    section.add(Box.createVerticalStrut(20));

    // Second row: Price and Stock
    JPanel priceStockRow = new JPanel(new GridLayout(1, 2, 20, 0));
    priceStockRow.setBackground(BACKGROUND_CARD);
    priceStockRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    priceStockRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, priceStockRow.getPreferredSize().height));

    priceStockRow.add(createFieldPanel("Price", priceField));
    priceStockRow.add(createFieldPanel("Stock Quantity ", stockField));
    section.add(priceStockRow);
    section.add(Box.createVerticalStrut(20));

    // Third row: Created By (full width)
    JPanel createdByPanel = createFieldPanel("Created By", createdByField);
    createdByPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    createdByPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, createdByPanel.getPreferredSize().height));
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
    JTextField field = new JTextField(placeholder);
    field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    field.setForeground(Color.GRAY); // placeholder color initially
    field.setBackground(Color.WHITE);
    field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 16, 12, 16)
    ));
    field.setPreferredSize(new Dimension(200, 44));

    field.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            // Change border on focus
            field.setBorder(new CompoundBorder(
                    new LineBorder(FOCUS_COLOR, 2, true),
                    new EmptyBorder(11, 15, 11, 15)
            ));
            // Remove placeholder text when focus gained
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(TEXT_PRIMARY); // normal text color
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            // Restore border on focus lost
            field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));
            // If field is empty, show placeholder
            if (field.getText().isEmpty()) {
                field.setForeground(Color.GRAY);
                field.setText(placeholder);
            }
        }
    });

    return field;
}

private JComboBox<Category> createCategoryComboBox() {
    CategoryDAO categoryDAO = new CategoryDAO();
    List<Category> categoryList = categoryDAO.getAllCategories();

    JComboBox<Category> combo = new JComboBox<>(categoryList.toArray(new Category[0]));

    combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    combo.setBackground(Color.WHITE);
    combo.setForeground(Color.DARK_GRAY);
    combo.setBorder(new CompoundBorder(
            new LineBorder(Color.GRAY, 1, true),
            new EmptyBorder(8, 16, 8, 16)
    ));
    combo.setPreferredSize(new Dimension(400, 55));
    combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    combo.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            combo.setBorder(new CompoundBorder(
                    new LineBorder(new Color(0x4682B4), 2, true),
                    new EmptyBorder(7, 15, 7, 15)
            ));
        }

        @Override
        public void focusLost(FocusEvent e) {
            combo.setBorder(new CompoundBorder(
                    new LineBorder(Color.GRAY, 1, true),
                    new EmptyBorder(8, 16, 8, 16)
            ));
        }
    });

    combo.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBorder(new EmptyBorder(0, 0, 0, 0));

            if (value instanceof Category) {
                setText(((Category) value).getCategoryName());
            }

            if (isSelected) {
                setBackground(new Color(0x4682B4));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.DARK_GRAY);
            }
            return this;
        }
    });

    return combo;
}

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setBorder(new EmptyBorder(0, 0, 0, 0));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(260, 55));

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

    private void handleCreateProduct() {
        try {
            String name = nameField.getText().trim();
            String categoryName = (String) categoryBox.getSelectedItem();
            String priceText = priceField.getText().trim();
            String stockText = stockField.getText().trim();
            String createdBy = createdByField.getText().trim();

            // Validation
                if (name.isEmpty() || name.equals("Product Name") ||
        priceText.isEmpty() || priceText.equals("0.00") ||
        stockText.isEmpty() || stockText.equals("0") ||
        createdBy.isEmpty() || createdBy.equals("Created By")) {
        showErrorDialog("Please fill in all required fields (marked with *)");
        return;
    }

            BigDecimal price;
            int stock;

            try {
                price = new BigDecimal(getPrice());
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

            LocalDateTime now = LocalDateTime.now();
            Category category = new Category(0, categoryName, "");
            Product product = new Product(0, name, category, price, stock, now, createdBy, now);

            boolean inserted = productDAO.insertProduct(product);
     if (!inserted) {
         showErrorDialog("Failed to save product to database.");
       return; 
     }

            // Update inventory
            InventoryManager.getInstance().updateStock(product, stock);

            // Success message
            showSuccessDialog("Product created successfully!\n\n" + "Name: " + name + "\n" + "Category: " + categoryName + "\n" +
                    "Price: $" + price + "\n" + "Stock: " + stock + " units");

            clearFields();

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
 nameField.setText("Product Name");
    nameField.setForeground(Color.GRAY);

    priceField.setText("0.00");
    priceField.setForeground(Color.GRAY);

    stockField.setText("0");
    stockField.setForeground(Color.GRAY);

    createdByField.setText("Created By");
    createdByField.setForeground(Color.GRAY);

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