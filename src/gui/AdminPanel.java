package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminPanel extends JPanel {
    private JTextField nameField, emailField, roleField;
    private JPasswordField passwordField;
    private JTable adminTable;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("👨‍💼 Admin Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE); // White is readable on PRIMARY
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Manage system administrators and their roles");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(ModernColors.TEXT_SECONDARY); // Lighter for contrast
        header.add(subtitle, BorderLayout.SOUTH);

        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new GridLayout(1, 2, 20, 0));
        main.setBackground(ModernColors.BACKGROUND);

        main.add(createFormPanel());
        main.add(createTablePanel());

        return main;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ModernColors.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Form title
        JLabel formTitle = new JLabel("➕ Add New Administrator");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(ModernColors.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(formTitle, gbc);

        nameField = createStyledTextField();
        emailField = createStyledTextField();
        roleField = createStyledTextField();
        passwordField = createStyledPasswordField();

        nameField.setToolTipText("Enter full name (e.g., John Doe)");
        emailField.setToolTipText("Enter valid email (e.g., john.doe@example.com)");
        roleField.setToolTipText("Enter role (e.g., Manager, Support)");
        passwordField.setToolTipText("Enter password (min 6 characters)");

        addEmailValidation(emailField);
        addPasswordValidation(passwordField);

        addFormField(panel, "👤 Full Name *", nameField, "e.g., John Doe", gbc, 1);
        addFormField(panel, "📧 Email Address *", emailField, "e.g., john.doe@example.com", gbc, 2);
        addFormField(panel, "🎯 Role", roleField, "e.g., Manager", gbc, 3);
        addFormField(panel, "🔒 Password *", passwordField, "Min 6 characters", gbc, 4);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ModernColors.WHITE);

        JButton saveButton = createPrimaryButton("Add Administrator");
        saveButton.addActionListener(e -> addAdmin());

        JButton clearButton = createSecondaryButton("Clear");
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernColors.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel tableTitle = new JLabel("📋 Current Administrators");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(ModernColors.TEXT_PRIMARY);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        String[] columns = {"Name", "Email", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        adminTable = createStyledTable();

        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.getViewport().setBackground(ModernColors.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel; // Fixed: Return JPanel, not JScrollPane
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(ModernColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(ModernColors.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        return field;
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setGridColor(ModernColors.BORDER);
        table.setSelectionBackground(ModernColors.SELECTION_BACKGROUND);
        table.setSelectionForeground(ModernColors.TEXT_PRIMARY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(ModernColors.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? ModernColors.WHITE : new Color(245, 247, 250));
                }
                c.setForeground(ModernColors.TEXT_PRIMARY);
                return c;
            }
        });

        return table;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ModernColors.PRIMARY);
        button.setForeground(Color.WHITE); // White on blue is readable
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ModernColors.PRIMARY);
            }
        });
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ModernColors.BORDER);
        button.setForeground(ModernColors.TEXT_PRIMARY);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ModernColors.BORDER.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ModernColors.BORDER);
            }
        });
        return button;
    }

    private void addFormField(JPanel panel, String label, JComponent field, String hint, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);

        gbc.gridx = 1; gbc.gridy = row + 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 10, 8, 10);
        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hintLabel.setForeground(ModernColors.TEXT_SECONDARY);
        panel.add(hintLabel, gbc);

        gbc.insets = new Insets(8, 10, 8, 10); // Reset insets
    }

    private void addEmailValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validate(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validate(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String email = field.getText();
                boolean isValid = email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isValid ? ModernColors.SUCCESS : ModernColors.ERROR, 1),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
    }

    private void addPasswordValidation(JPasswordField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validate(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validate(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String pass = new String(field.getPassword());
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                        pass.length() >= 6 ? ModernColors.SUCCESS : ModernColors.ERROR, 1),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
    }

    private void addAdmin() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleField.getText().trim();
        String password = new String(passwordField.getPassword());

        StringBuilder errors = new StringBuilder();
        if (name.isEmpty()) errors.append("Full Name is required.\n");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) errors.append("Valid Email is required.\n");
        if (password.length() < 6) errors.append("Password must be at least 6 characters.\n");

        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this, errors.toString(), 
                "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{name, email, role});
        clearFields();
        
        JOptionPane.showMessageDialog(this, "Administrator added successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        roleField.setText("");
        passwordField.setText("");
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
    }
}