package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import gui.ProductPanel;
import gui.CustomerPanel;
import gui.OrderPanel;

public class RetailDashboard extends JFrame {
    public RetailDashboard() {
        setTitle("Online Retail System - Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(ModernColors.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("🏪 Online Retail System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Your centralized management hub");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        header.add(subtitle, BorderLayout.SOUTH);

        return header;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(ModernColors.BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Create buttons
        JButton customerBtn = createActionButton("👤 Customers", "Manage customer records", ModernColors.SUCCESS);
        JButton productBtn = createActionButton("📦 Products", "Manage inventory", ModernColors.INFO);
        JButton orderBtn = createActionButton("🛒 Orders", "Process customer orders", ModernColors.WARNING);
        JButton paymentBtn = createActionButton("💳 Payments", "Handle transactions", ModernColors.PRIMARY);

        // Add navigation logic
        customerBtn.addActionListener(e -> openPanel(new CustomerPanel()));
        productBtn.addActionListener(e -> openPanel(new ProductPanel()));
        orderBtn.addActionListener(e -> openPanel(new OrderPanel()));
        paymentBtn.addActionListener(e -> openPanel(new PaymentPanel()));

        panel.add(customerBtn);
        panel.add(productBtn);
        panel.add(orderBtn);
        panel.add(paymentBtn);

        return panel;
    }

    private JButton createActionButton(String title, String description, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(color.brighter().brighter());
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color.darker());
        button.add(titleLabel, BorderLayout.NORTH);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(ModernColors.TEXT_SECONDARY);
        descLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        button.add(descLabel, BorderLayout.SOUTH);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color);
                titleLabel.setForeground(Color.WHITE);
                descLabel.setForeground(new Color(220, 220, 220));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color.brighter().brighter());
                titleLabel.setForeground(color.darker());
                descLabel.setForeground(ModernColors.TEXT_SECONDARY);
            }
        });

        return button;
    }

    private void openPanel(JPanel panel) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RetailDashboard();
    }
}