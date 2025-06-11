package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import onlineretailsystem.ModelClasses.Category;
import onlineretailsystem.CategoryDAO;

public class CategoryPanel extends JPanel {
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, descField;
    private JButton addButton, updateButton, deleteButton;
    private CategoryDAO categoryDAO = new CategoryDAO();

    public CategoryPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadCategoryData();
        setupEventListeners();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("📂 Category Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Organize your product categories");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        header.add(subtitle, BorderLayout.SOUTH);

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
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = createStyledTextField();
        descField = createStyledTextField();

        addFormField(panel, "Category Name", nameField, gbc, 0, 0);
        addFormField(panel, "Description", descField, gbc, 2, 0);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("Categories");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ModernColors.TEXT_PRIMARY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0);
        categoryTable = createStyledTable();
        
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBackground(ModernColors.BACKGROUND);

        addButton = createActionButton("Add", ModernColors.SUCCESS, "✓");
        updateButton = createActionButton("Update", ModernColors.WARNING, "✏");
        deleteButton = createActionButton("Delete", ModernColors.ERROR, "✗");

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return field;
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

    private JButton createActionButton(String text, Color bgColor, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x; gbc.gridy = y; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = x + 1; gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.getSelectedRow();
                if (row >= 0) {
                    nameField.setText(tableModel.getValueAt(row, 1).toString());
                    descField.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void loadCategoryData() {
        tableModel.setRowCount(0);
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category c : categories) {
            tableModel.addRow(new Object[]{c.getCategoryId(), c.getCategoryName(), c.getDescription()});
        }
    }

    private void addCategory() {
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        
        if (name.isEmpty()) {
            showMessage("Category name is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Category newCategory = new Category(0, name, desc);
        categoryDAO.insertCategory(newCategory);
        loadCategoryData();
        clearFields();
        showMessage("Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select a category to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String name = nameField.getText().trim();
        String desc = descField.getText().trim();
        
        Category updatedCategory = new Category(id, name, desc);
        categoryDAO.updateCategory(updatedCategory);
        loadCategoryData();
        clearFields();
        showMessage("Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            showMessage("Please select a category to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this category?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            categoryDAO.deleteCategory(id);
            loadCategoryData();
            clearFields();
            showMessage("Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        descField.setText("");
        categoryTable.clearSelection();
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}