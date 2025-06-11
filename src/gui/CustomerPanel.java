package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JTextField addressField, cityField, stateField, postalCodeField, countryField;

    public CustomerPanel() {
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

        JLabel title = new JLabel("👤 Customer Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Add new customers to the system");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        header.add(subtitle, BorderLayout.SOUTH);

        return header;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernColors.BACKGROUND);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Initialize fields
        firstNameField = createStyledTextField();
        lastNameField = createStyledTextField();
        emailField = createStyledTextField();
        phoneField = createStyledTextField();
        addressField = createStyledTextField();
        cityField = createStyledTextField();
        stateField = createStyledTextField();
        postalCodeField = createStyledTextField();
        countryField = createStyledTextField();

        // Add validation
        addEmailValidation(emailField);
        addPhoneValidation(phoneField);

        // Personal Information Section
        addSectionTitle(formPanel, "Personal Information", gbc, 0);
        addFormField(formPanel, "First Name", firstNameField, gbc, 1, 0);
        addFormField(formPanel, "Last Name", lastNameField, gbc, 1, 2);
        addFormField(formPanel, "Email Address", emailField, gbc, 2, 0);
        addFormField(formPanel, "Phone Number", phoneField, gbc, 2, 2);

        // Address Information Section
        addSectionTitle(formPanel, "Address Information", gbc, 3);
        addFormField(formPanel, "Street Address", addressField, gbc, 4, 0, 4);
        addFormField(formPanel, "City", cityField, gbc, 5, 0);
        addFormField(formPanel, "State/Province", stateField, gbc, 5, 2);
        addFormField(formPanel, "Postal Code", postalCodeField, gbc, 6, 0);
        addFormField(formPanel, "Country", countryField, gbc, 6, 2);

        // Save Button
        JButton saveButton = createPrimaryButton("Save Customer");
        saveButton.addActionListener(e -> saveCustomer());
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(saveButton, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        return mainPanel;
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

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ModernColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addSectionTitle(JPanel panel, String title, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        gbc.insets = new Insets(row == 0 ? 0 : 25, 0, 15, 0);
        
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(ModernColors.PRIMARY);
        sectionLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ModernColors.PRIMARY));
        
        panel.add(sectionLabel, gbc);
        gbc.insets = new Insets(12, 10, 12, 10); // Reset insets
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row, int col) {
        addFormField(panel, label, field, gbc, row, col, 1);
    }

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row, int col, int width) {
        gbc.gridx = col; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(ModernColors.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col; gbc.gridy = row + 1; gbc.gridwidth = width; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void addEmailValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String email = field.getText().trim();
                boolean isValid = email.contains("@") && email.contains(".");
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isValid ? ModernColors.SUCCESS : ModernColors.ERROR, 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private void addPhoneValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String phone = field.getText().trim();
                boolean isValid = phone.matches("\\d{10,15}");
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isValid ? ModernColors.SUCCESS : ModernColors.ERROR, 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private void saveCustomer() {
        // Validate required fields
        if (firstNameField.getText().trim().isEmpty() || 
            lastNameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields (First Name, Last Name, Email).", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate phone number format
        String phone = phoneField.getText().trim();
        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid phone number (10-15 digits).", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If all validations pass, proceed with saving the customer
        // ...
    }
}