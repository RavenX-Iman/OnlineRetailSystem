package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.CustomerPanel;
import gui.ProductPanel;
import gui.ProductPanel;
import gui.OrderPanel;

public class RetailDashboard extends JFrame {

    public RetailDashboard() {
        // Set title and window size
        setTitle("Online Retail System - Dashboard");
        setSize(800, 600);  // Oversized for better view
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center on screen

        // Create a main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Heading at the top
        JLabel headingLabel = new JLabel("Welcome to Online Retail System", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(headingLabel, BorderLayout.NORTH);

        // Create a panel for buttons in the center
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 30, 30)); // 2x2 grid with spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60)); // Padding

        // Create buttons for each module
        JButton customerBtn = new JButton("Customers");
        JButton productBtn = new JButton("Products");
        JButton orderBtn = new JButton("Orders");
        JButton paymentBtn = new JButton("Payments");

        // Style the buttons
        JButton[] buttons = { customerBtn, productBtn, orderBtn, paymentBtn };
        for (JButton btn : buttons) {
            btn.setFont(new Font("Arial", Font.PLAIN, 22));
            btn.setPreferredSize(new Dimension(200, 100));
        }

        // Add buttons to the panel
        buttonPanel.add(customerBtn);
        buttonPanel.add(productBtn);
        buttonPanel.add(orderBtn);
        buttonPanel.add(paymentBtn);

        // Add button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);

        // Set frame visible
        setVisible(true);

        // Action listeners for buttons (you can open actual forms or show messages here)
        customerBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Customer Panel Coming Soon!"));
        productBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Product Panel Coming Soon!"));
        orderBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Order Panel Coming Soon!"));
        paymentBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Payment Panel Coming Soon!"));
    }

    // Main method to run this frame
    public static void main(String[] args) {
        // Optional: Use system theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new RetailDashboard();  // Launch the dashboard
    }
}
