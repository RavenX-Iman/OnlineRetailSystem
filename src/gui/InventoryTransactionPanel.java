package gui;

import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.ModelClasses.Product;
import onlineretailsystem.ModelClasses.InventoryTransaction.TransactionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryTransactionPanel extends JPanel {
    private JTextField productNameField, priceField, quantityField, reasonField;
    private JComboBox<TransactionType> typeComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

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

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ModernColors.PRIMARY);
        
        JLabel title = new JLabel("📦 Inventory Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Track and manage inventory movements");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        subtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
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
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize fields
        productNameField = createStyledTextField();
        priceField = createStyledTextField();
        quantityField = createStyledTextField();
        reasonField = createStyledTextField();
        typeComboBox = createStyledComboBox();

        // Add form title
        JLabel formTitle = new JLabel("➕ Add New Transaction");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(formTitle, gbc);

        // Form fields
        addFormField(panel, "📦 Product Name", productNameField, gbc, 1, 0);
        addFormField(panel, "💰 Product Price", priceField, gbc, 1, 2);
        addFormField(panel, "🔢 Quantity", quantityField, gbc, 2, 0);
        addFormField(panel, "🔄 Transaction Type", typeComboBox, gbc, 2, 2);
        
        // Reason field spans full width
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel reasonLabel = new JLabel("📝 Reason");
        reasonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonLabel.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(reasonLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weightx = 1.0;
        panel.add(reasonField, gbc);

        // Add button
        JButton addButton = createPrimaryButton("Add Transaction");
        addButton.addActionListener(e -> addTransaction());

        gbc.gridy = 5; gbc.gridwidth = 4;
        gbc.insets = new Insets(25, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
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
        
        // Hover effect
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

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = 1; gbc.weightx = 0.5;
        panel.add(field, gbc);
    }

    private void addTransaction() {
        try {
            String productName = productNameField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String reason = reasonField.getText().trim();
            TransactionType type = (TransactionType) typeComboBox.getSelectedItem();

            // Validation
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

            // Add to table
            String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
            String typeDisplay = type.toString().replace("_", " ");
            
            tableModel.addRow(new Object[]{
                productName, 
                typeDisplay, 
                quantity, 
                "$" + price.toString(), 
                reason.isEmpty() ? "N/A" : reason, 
                formattedDate
            });

            clearFields();
            showMessage("Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

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
        // Add some sample data
        tableModel.addRow(new Object[]{"Laptop Pro", "STOCK_IN", "10", "$999.99", "New shipment", "Dec 10, 2024 14:30"});
        tableModel.addRow(new Object[]{"Wireless Mouse", "STOCK_OUT", "5", "$29.99", "Customer purchase", "Dec 10, 2024 15:45"});
        tableModel.addRow(new Object[]{"Gaming Keyboard", "ADJUSTMENT", "2", "$79.99", "Damaged items", "Dec 09, 2024 11:20"});
    }
}