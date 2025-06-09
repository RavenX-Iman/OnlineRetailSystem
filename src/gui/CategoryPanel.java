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

    private CategoryDAO categoryDAO = new CategoryDAO(); // Use DAO directly

    public CategoryPanel() {
        setLayout(new BorderLayout());

        // Top Input Form
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        nameField = new JTextField();
        descField = new JTextField();

        inputPanel.add(new JLabel("Category Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descField);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description"}, 0);
        categoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data into table
        loadCategoryData();

        // Listeners
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        // When a row is selected, populate fields
        categoryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = categoryTable.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                descField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });
    }

    private void loadCategoryData() {
        tableModel.setRowCount(0); // Clear table
        List<Category> categories = categoryDAO.getAllCategories(); // Use DAO
        for (Category c : categories) {
            tableModel.addRow(new Object[]{c.getCategoryId(), c.getCategoryName(), c.getDescription()});
        }
    }

    private void addCategory() {
        String name = nameField.getText();
        String desc = descField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name is required.");
            return;
        }
        Category newCategory = new Category(0, name, desc);
        categoryDAO.insertCategory(newCategory); // Use DAO
        loadCategoryData();
        clearFields();
    }

    private void updateCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = nameField.getText();
        String desc = descField.getText();
        Category updatedCategory = new Category(id, name, desc);
        categoryDAO.updateCategory(updatedCategory); // Use DAO
        loadCategoryData();
        clearFields();
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        categoryDAO.deleteCategory(id); // Use DAO
        loadCategoryData();
        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        descField.setText("");
        categoryTable.clearSelection();
    }
}
