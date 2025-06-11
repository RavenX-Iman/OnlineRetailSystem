package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends JPanel {
    private JLabel timeLabel;
    private Timer timer;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooterSection(), BorderLayout.SOUTH);
        
        startClock();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ModernColors.PRIMARY);
        
        JLabel title = new JLabel("🏪 Retail System Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.NORTH);
        
        JLabel subtitle = new JLabel("Welcome back! Here's what's happening with your store today.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        subtitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Time display
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(timeLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(ModernColors.BACKGROUND);
        
        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(ModernColors.BACKGROUND);
        
        statsPanel.add(createStatCard("👥 Total Customers", "1,245", "+12 today", ModernColors.SUCCESS));
        statsPanel.add(createStatCard("📦 Total Products", "892", "+5 this week", ModernColors.INFO));
        statsPanel.add(createStatCard("⏳ Pending Orders", "67", "Needs attention", ModernColors.WARNING));
        statsPanel.add(createStatCard("💰 Total Revenue", "$45,230", "+8.5% this month", ModernColors.PRIMARY));
        
        main.add(statsPanel, BorderLayout.NORTH);
        
        // Quick actions
        JPanel actionsPanel = createQuickActionsPanel();
        main.add(actionsPanel, BorderLayout.CENTER);
        
        return main;
    }
    
    private JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Title with icon
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(ModernColors.TEXT_SECONDARY);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Main value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        card.add(valueLabel, BorderLayout.CENTER);
        
        // Subtitle
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(ModernColors.TEXT_SECONDARY);
        card.add(subLabel, BorderLayout.SOUTH);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ModernColors.BORDER, 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return card;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        JLabel actionTitle = new JLabel("⚡ Quick Actions");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        actionTitle.setForeground(ModernColors.TEXT_PRIMARY);
        actionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(actionTitle, BorderLayout.NORTH);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setBackground(Color.WHITE);
        
        buttonsPanel.add(createActionButton("👤 Add Customer", "Register new customers", ModernColors.SUCCESS));
        buttonsPanel.add(createActionButton("📦 Add Product", "Expand your inventory", ModernColors.INFO));
        buttonsPanel.add(createActionButton("📋 View Orders", "Manage customer orders", ModernColors.WARNING));
        buttonsPanel.add(createActionButton("📊 Reports", "Generate business insights", ModernColors.PRIMARY));
        
        panel.add(buttonsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JButton createActionButton(String title, String description, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(color.brighter().brighter());
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(color.darker());
        button.add(titleLabel, BorderLayout.NORTH);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(ModernColors.TEXT_SECONDARY);
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        button.add(descLabel, BorderLayout.SOUTH);
        
        // Hover effects
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
        
        button.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Navigate to " + title.substring(2) + " section", 
                "Quick Action", 
                JOptionPane.INFORMATION_MESSAGE));
        
        return button;
    }
    
    private JPanel createFooterSection() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel activityTitle = new JLabel("📈 Recent Activity");
        activityTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        activityTitle.setForeground(ModernColors.TEXT_PRIMARY);
        activityTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        footer.add(activityTitle, BorderLayout.NORTH);
        
        // Activity list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("🛒 New order #1234 received from John Doe - 2 minutes ago");
        listModel.addElement("📦 Product 'Wireless Headphones' added to inventory - 15 minutes ago");
        listModel.addElement("👤 New customer registration: Jane Smith - 1 hour ago");
        listModel.addElement("💰 Payment of $299.99 processed for order #1233 - 2 hours ago");
        listModel.addElement("📊 Daily sales report generated successfully - 3 hours ago");
        
        JList<String> activityList = new JList<>(listModel);
        activityList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityList.setBackground(ModernColors.BACKGROUND);
        activityList.setSelectionBackground(new Color(240, 245, 255));
        activityList.setSelectionForeground(ModernColors.TEXT_PRIMARY);
        activityList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setPreferredSize(new Dimension(0, 120));
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernColors.BORDER));
        scrollPane.getViewport().setBackground(ModernColors.BACKGROUND);
        footer.add(scrollPane, BorderLayout.CENTER);
        
        return footer;
    }
    
    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
        updateTime();
    }
    
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy • HH:mm:ss");
        timeLabel.setText(sdf.format(new Date()));
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) {
            timer.stop();
        }
    }
}