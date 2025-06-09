package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField, cityField, stateField, postalCodeField, countryField;

    public CustomerPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Add New Customer");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Add New Customer"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // First Name
        addLabel("First Name:", gbc, row, 0);
        firstNameField = new JTextField();
        addField(firstNameField, gbc, row++, 1);

        // Last Name
        addLabel("Last Name:", gbc, row, 0);
        lastNameField = new JTextField();
        addField(lastNameField, gbc, row++, 1);

        // Email
        addLabel("Email:", gbc, row, 0);
        emailField = new JTextField();
        addField(emailField, gbc, row++, 1);
        addEmailValidation(emailField);

        // Phone
        addLabel("Phone:", gbc, row, 0);
        phoneField = new JTextField();
        addField(phoneField, gbc, row++, 1);
        addPhoneValidation(phoneField);

        // Address
        addLabel("Address:", gbc, row, 0);
        addressField = new JTextField();
        addField(addressField, gbc, row++, 1);

        // City
        addLabel("City:", gbc, row, 0);
        cityField = new JTextField();
        addField(cityField, gbc, row++, 1);

        // State
        addLabel("State:", gbc, row, 0);
        stateField = new JTextField();
        addField(stateField, gbc, row++, 1);

        // Postal Code
        addLabel("Postal Code:", gbc, row, 0);
        postalCodeField = new JTextField();
        addField(postalCodeField, gbc, row++, 1);

        // Country
        addLabel("Country:", gbc, row, 0);
        countryField = new JTextField();
        addField(countryField, gbc, row++, 1);

        // Save Button
        JButton saveButton = new JButton("Save Customer");
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(saveButton, gbc);
    }

    // Reusable method to add labels
    private void addLabel(String text, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        add(new JLabel(text), gbc);
    }

    // Reusable method to add fields
    private void addField(JTextField field, GridBagConstraints gbc, int row, int col) {
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        add(field, gbc);
    }

    // ===== Validation Methods =====

    private void addEmailValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                String email = field.getText().trim();
                if (email.contains("@") && email.contains(".")) {
                    field.setBackground(Color.WHITE);
                } else {
                    field.setBackground(Color.PINK);
                }
            }
        });
    }

    private void addPhoneValidation(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void changedUpdate(DocumentEvent e) { validate(); }

            private void validate() {
                if (field.getText().matches("\\d{10,15}")) {
                    field.setBackground(Color.WHITE);
                } else {
                    field.setBackground(Color.PINK);
                }
            }
        });
    }
}
