package gui;

import onlineretailsystem.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleCombo;

    public LoginPanel(MainFrame mainFrame) {
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        // Font styling
        Font labelFont = new Font("SansSerif", Font.BOLD, 16); // Bold and larger

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setOpaque(false);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(labelFont);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setOpaque(false);
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(labelFont);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        add(passwordField);

        // Role
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setOpaque(false);
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(labelFont);
        add(roleLabel);

        roleCombo = new JComboBox<>(new String[]{"Admin", "Customer"});
        roleCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        add(roleCombo);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(new JLabel()); // empty label for spacing
        add(loginButton);

        // Login logic
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (username.equals("admin") && password.equals("admin123") && role.equals("Admin")) {
                mainFrame.showAdminDashboard();
            } else if (username.equals("customer") && password.equals("cust123") && role.equals("Customer")) {
                mainFrame.showCustomerDashboard(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or role!");
            }
        });
    }
}
