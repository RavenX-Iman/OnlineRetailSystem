package gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import onlineretailsystem.CustomerDAO;
import onlineretailsystem.ModelClasses.Customer;
import java.sql.Connection;
import onlineretailsystem.DBConnection;

public class CustomerPanel extends JPanel {

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

    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JTextField addressField, cityField, stateField, postalCodeField, countryField;
    private JButton saveButton, clearButton, cancelButton;

    public CustomerPanel() {
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
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_MAIN);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Main title
        JLabel titleLabel = new JLabel("👤 Customer Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Enter customer details to add them to the system");
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

        // Create card-style form panel
        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(BACKGROUND_CARD);
        formCard.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(35, 35, 35, 35)
        ));

        // Add form sections
        formCard.add(createPersonalInfoSection());
        formCard.add(Box.createVerticalStrut(30));
        formCard.add(createAddressInfoSection());

        mainPanel.add(formCard, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createPersonalInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND_CARD);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header
        JLabel sectionLabel = createSectionLabel("Personal Information");
        section.add(sectionLabel);
        section.add(Box.createVerticalStrut(20));

        // First row: First Name and Last Name
        JPanel nameRow = new JPanel(new GridLayout(1, 2, 20, 0));
        nameRow.setBackground(BACKGROUND_CARD);

        firstNameField = createStyledTextField("First Name");
        lastNameField = createStyledTextField("Last Name");

        nameRow.add(createFieldPanel("First Name *", firstNameField));
        nameRow.add(createFieldPanel("Last Name *", lastNameField));
        section.add(nameRow);
        section.add(Box.createVerticalStrut(20));

        // Second row: Email and Phone
        JPanel contactRow = new JPanel(new GridLayout(1, 2, 20, 0));
        contactRow.setBackground(BACKGROUND_CARD);

        emailField = createStyledTextField("Email Address");
        phoneField = createStyledTextField("Phone Number");

        contactRow.add(createFieldPanel("Email Address *", emailField));
        contactRow.add(createFieldPanel("Phone Number", phoneField));
        section.add(contactRow);

        return section;
    }

    private JPanel createAddressInfoSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(BACKGROUND_CARD);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Section header
        JLabel sectionLabel = createSectionLabel("Address Information");
        section.add(sectionLabel);
        section.add(Box.createVerticalStrut(20));

        // Street Address (full width)
        addressField = createStyledTextField("Street Address");
        JPanel addressPanel = createFieldPanel("Street Address", addressField);
        addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, addressPanel.getPreferredSize().height));
        section.add(addressPanel);
        section.add(Box.createVerticalStrut(20));

        // City and State row
        JPanel cityStateRow = new JPanel(new GridLayout(1, 2, 20, 0));
        cityStateRow.setBackground(BACKGROUND_CARD);
        cityStateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cityStateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, cityStateRow.getPreferredSize().height));

        cityField = createStyledTextField("City");
        stateField = createStyledTextField("State/Province");

        cityStateRow.add(createFieldPanel("City", cityField));
        cityStateRow.add(createFieldPanel("State/Province", stateField));
        section.add(cityStateRow);
        section.add(Box.createVerticalStrut(20));

        // Postal Code and Country row
        JPanel postalCountryRow = new JPanel(new GridLayout(1, 2, 20, 0));
        postalCountryRow.setBackground(BACKGROUND_CARD);
        postalCountryRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        postalCountryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, postalCountryRow.getPreferredSize().height));

        postalCodeField = createStyledTextField("Postal Code");
        countryField = createStyledTextField("Country");

        postalCountryRow.add(createFieldPanel("Postal Code", postalCodeField));
        postalCountryRow.add(createFieldPanel("Country", countryField));
        section.add(postalCountryRow);

        return section;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(BACKGROUND_MAIN);
        buttonPanel.setBorder(new EmptyBorder(25, 0, 0, 0));

        // Clear button
        clearButton = createStyledButton("Clear", BORDER_COLOR, TEXT_SECONDARY);
        clearButton.addActionListener(e -> clearAllFields());

        // Cancel button
        cancelButton = createStyledButton("Cancel", BORDER_COLOR, TEXT_SECONDARY);

        // Save button (primary)
        saveButton = createPrimaryButton("💾 Save Customer");
        saveButton.addActionListener(e -> saveCustomer());

        // Add buttons with spacing
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(PRIMARY_BLUE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_CARD);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));

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

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 44));

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

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(textColor);
        button.setBackground(Color.WHITE);
        button.setBorder(new CompoundBorder(
                new LineBorder(bgColor, 1, true),
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

    private void clearAllFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        cityField.setText("");
        stateField.setText("");
        postalCodeField.setText("");
        countryField.setText("");
        firstNameField.requestFocus();
    }

    private void saveCustomer() {
        // Basic validation
        if (firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (marked with *)",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Success message
        try {
    // Create Customer object
    Customer customer = new Customer();
    customer.setFirstName(getFirstName());
    customer.setLastName(getLastName());
    customer.setEmail(getEmail());
    customer.setPhone(getPhone());
    customer.setAddress(getAddress());
    customer.setCity(getCity());
    customer.setState(getState());
    customer.setPostalCode(getPostalCode());
    customer.setCountry(getCountry());

    // Save to DB via DAO
    
   Connection conn = DBConnection.getConnection();
CustomerDAO customerDAO = CustomerDAO.getInstance(conn);
boolean success = customerDAO.insertCustomer(customer);


    if (success) {
        JOptionPane.showMessageDialog(this,
                "Customer saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        clearAllFields();
    } else {
        JOptionPane.showMessageDialog(this,
                "Failed to save customer.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this,
            "An error occurred: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
}
 
        //(replace with actual save logic)
        // JOptionPane.showMessageDialog(this,
        //         "Customer saved successfully!",
        //         "Success",
        //         JOptionPane.INFORMATION_MESSAGE);

        // // Clear form after successful save
        // clearAllFields();
    }

    // Getter methods for accessing field values
    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getLastName() { return lastNameField.getText().trim(); }
    public String getEmail() { return emailField.getText().trim(); }
    public String getPhone() { return phoneField.getText().trim(); }
    public String getAddress() { return addressField.getText().trim(); }
    public String getCity() { return cityField.getText().trim(); }
    public String getState() { return stateField.getText().trim(); }
    public String getPostalCode() { return postalCodeField.getText().trim(); }
    public String getCountry() { return countryField.getText().trim(); }
}