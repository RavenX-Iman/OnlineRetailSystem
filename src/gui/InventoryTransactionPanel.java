package gui;

import onlineretailsystem.InventoryTransactionDAO;
import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.ModelClasses.InventoryTransaction.TransactionType;
import onlineretailsystem.ModelClasses.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryTransactionPanel extends JPanel {
    private JTextField productNameField, priceField, quantityField, reasonField;
    private JComboBox<TransactionType> typeComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private final InventoryTransactionDAO transactionDAO = new InventoryTransactionDAO();


    public InventoryTransactionPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadSampleData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("📦 Inventory Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Track and manage inventory movements");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        subtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ModernColors.PRIMARY);
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(ModernColors.BACKGROUND);
        main.add(createFormPanel(), BorderLayout.NORTH);
        main.add(createTablePanel(), BorderLayout.CENTER);
        return main;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 - Title
        JLabel formTitle = new JLabel("➕ Add New Transaction");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(formTitle, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;

        // Row 1 - Product Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("📦 Product Name:"), gbc);
        gbc.gridx = 1;
        productNameField = new JTextField(20);
        panel.add(productNameField, gbc);

        // Row 2 - Product Price
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("💰 Product Price:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        panel.add(priceField, gbc);

        // Row 3 - Quantity
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("🔢 Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        panel.add(quantityField, gbc);

        // Row 4 - Transaction Type
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("🔄 Transaction Type:"), gbc);
        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(TransactionType.values());
        panel.add(typeComboBox, gbc);

        // Row 5 - Reason
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("📝 Reason:"), gbc);
        gbc.gridx = 1;
        reasonField = new JTextField(20);
        panel.add(reasonField, gbc);

        // Row 6 - Add Button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Transaction");
        addButton.addActionListener(e -> addTransaction());
        panel.add(addButton, gbc);

        return panel;
    }


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel tableTitle = new JLabel("📋 Transaction History");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ModernColors.TEXT_PRIMARY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"Product", "Type", "Quantity", "Price", "Reason", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = createStyledTable();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridy = row + 1; gbc.weightx = 0.5;
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

    private JComboBox<TransactionType> createStyledComboBox() {
        JComboBox<TransactionType> combo = new JComboBox<>(TransactionType.values());
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER, 1));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(ModernColors.BORDER);
        table.setSelectionBackground(new Color(240, 245, 255));
        table.setSelectionForeground(ModernColors.TEXT_PRIMARY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(ModernColors.BACKGROUND);
        table.getTableHeader().setForeground(ModernColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ModernColors.BORDER));
        return table;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ModernColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY);
            }
        });
        return button;
    }

private void addTransaction() {
    try {
        // Step 1: Read and validate input
        String productName = productNameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String reason = reasonField.getText().trim();
        TransactionType type = (TransactionType) typeComboBox.getSelectedItem();

        if (productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            showMessage("Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal price = new BigDecimal(priceText);
        int quantity = Integer.parseInt(quantityText);

        if (price.compareTo(BigDecimal.ZERO) <= 0 || quantity <= 0) {
            showMessage("Price and quantity must be positive values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 2: Create transaction object
        Product dummyProduct = new Product(); // To be replaced with real product lookup
        dummyProduct.setProductId(1); // Placeholder
        dummyProduct.setProductName(productName); // For display
        dummyProduct.setPrice(price); // Not used in DAO, but useful here

        InventoryTransaction tx = new InventoryTransaction();
        tx.setProduct(dummyProduct);
        tx.setTransactionType(type);
        tx.setQuantity(quantity);
        tx.setReason(reason.isEmpty() ? "N/A" : reason);
        tx.setCreatedAt(LocalDateTime.now());

        // Step 3: Insert into DB
        boolean success = transactionDAO.insert(tx);
        if (success) {
            // Step 4: Update table
            tableModel.addRow(new Object[]{
                productName,
                type.toString().replace("_", " "),
                quantity,
                "$" + price,
                reason.isEmpty() ? "N/A" : reason,
                tx.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))
            });

            clearFields();
            showMessage("Transaction added and saved to DB!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Failed to insert into database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException ex) {
        showMessage("Please enter valid numbers for price and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        showMessage("Error adding transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void clearFields() {
        productNameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        reasonField.setText("");
        typeComboBox.setSelectedIndex(0);
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

private void loadSampleData() {
    tableModel.setRowCount(0); // Add this at the start of loadSampleData()
    // Load all transactions from the DAO
    List<InventoryTransaction> transactions = transactionDAO.getAllTransactions();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();

    for (InventoryTransaction tx : transactions) {
        Product product = tx.getProduct();
        
        // Fallbacks in case of missing product info
        String productName = (product != null && product.getProductName() != null) ? product.getProductName() : "Unknown";
        String price = (product != null && product.getPrice() != null) ? currencyFormatter.format(product.getPrice()) : "$0.00";

        tableModel.addRow(new Object[]{
            productName,
            tx.getTransactionType().toString().replace("_", " "),
            tx.getQuantity(),
            price,
            tx.getReason() != null ? tx.getReason() : "N/A",
            tx.getCreatedAt().format(dateFormatter)
        });
    }
}

}
