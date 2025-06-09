package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    private JTextField nameField, emailField, roleField;
    private JPasswordField passwordField;
    private JTable adminTable;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Admin Management"));

        // === Top Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        addFormLabel(formPanel, "Name:", gbc, 0, 0);
        nameField = new JTextField(20);
        addFormField(formPanel, nameField, gbc, 0, 1);

        // Email
        addFormLabel(formPanel, "Email:", gbc, 1, 0);
        emailField = new JTextField(20);
        addFormField(formPanel, emailField, gbc, 1, 1);
        addEmailValidation(emailField);

        // Role
        addFormLabel(formPanel, "Role:", gbc, 2, 0);
        roleField = new JTextField(20);
        addFormField(formPanel, roleField, gbc, 2, 1);

        // Password
        addFormLabel(formPanel, "Password:", gbc, 3, 0);
        passwordField = new JPasswordField(20);
        addFormField(formPanel, passwordField, gbc, 3, 1);
        addPasswordValidation(passwordField);

        // Save Button
        JButton saveButton = new JButton("Add Admin");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(saveButton, gbc);

        // === Table Panel ===
        String[] columns = { "Name", "Email", "Role" };
        tableModel = new DefaultTableModel(columns, 0);
        adminTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(adminTable);

        // Add action listener for Save button
        saveButton.addActionListener(e -> addAdmin());

        add(formPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    // Add label to form
    private void addFormLabel(JPanel panel, String text, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(new JLabel(text), gbc);
    }

    // Add field to form
    private void addFormField(JPanel panel, JComponent field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(field, gbc);
    }

    // Email validation
    private void addEmailValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String email = field.getText();
                if (email.contains("@") && email.contains(".")) {
                    field.setBackground(Color.WHITE);
                } else {
                    field.setBackground(Color.PINK);
                }
            }
        });
    }

    // Password validation
    private void addPasswordValidation(JPasswordField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String pass = new String(field.getPassword());
                if (pass.length() >= 6) {
                    field.setBackground(Color.WHITE);
                } else {
                    field.setBackground(Color.PINK);
                }
            }
        });
    }

    // Add admin to the table
    private void addAdmin() {
        String name = nameField.getText();
        String email = emailField.getText();
        String role = roleField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{name, email, role});
        clearFields();
    }

    // Clear form fields after adding
    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        roleField.setText("");
        passwordField.setText("");
    }
}
