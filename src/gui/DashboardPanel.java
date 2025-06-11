package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardPanel extends JPanel {
    private JLabel timeLabel;
    private Timer timer;
    
    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        initializeComponents();
        startClock();
    }
    
    private void initializeComponents() {
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        JPanel mainPanel = createMainContentPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Welcome Label
        JLabel welcomeLabel = new JLabel("Online Retail System Dashboard");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // Time Label
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setForeground(Color.WHITE);
        headerPanel.add(timeLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        // Statistics Cards
        mainPanel.add(createStatisticsCard("Total Customers", "1,245", new Color(52, 152, 219)));
        mainPanel.add(createStatisticsCard("Total Products", "892", new Color(46, 204, 113)));
        mainPanel.add(createStatisticsCard("Pending Orders", "67", new Color(241, 196, 15)));
        mainPanel.add(createStatisticsCard("Total Revenue", "$45,230", new Color(155, 89, 182)));
        
        return mainPanel;
    }
    
    private JPanel createStatisticsCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);
        
        // Icon area (placeholder)
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(iconLabel, BorderLayout.EAST);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Quick Actions Panel
        JPanel quickActionsPanel = createQuickActionsPanel();
        footerPanel.add(quickActionsPanel, BorderLayout.CENTER);
        
        // Recent Activity Panel
        JPanel activityPanel = createRecentActivityPanel();
        footerPanel.add(activityPanel, BorderLayout.SOUTH);
        
        return footerPanel;
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(new TitledBorder("Quick Actions"));
        
        // Quick action buttons
        JButton addCustomerBtn = createActionButton("Add Customer", new Color(52, 152, 219));
        JButton addProductBtn = createActionButton("Add Product", new Color(46, 204, 113));
        JButton viewOrdersBtn = createActionButton("View Orders", new Color(241, 196, 15));
        JButton reportsBtn = createActionButton("Generate Report", new Color(155, 89, 182));
        
        actionsPanel.add(addCustomerBtn);
        actionsPanel.add(addProductBtn);
        actionsPanel.add(viewOrdersBtn);
        actionsPanel.add(reportsBtn);
        
        return actionsPanel;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        // Add click action (placeholder)
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "This will navigate to " + text + " functionality", 
                "Quick Action", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        return button;
    }
    
    private JPanel createRecentActivityPanel() {
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBackground(Color.WHITE);
        activityPanel.setBorder(new TitledBorder("Recent Activity"));
        activityPanel.setPreferredSize(new Dimension(0, 120));
        
        // Activity list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("🛒 New order #1234 received from John Doe");
        listModel.addElement("📦 Product 'Laptop Pro' added to inventory");
        listModel.addElement("👤 New customer registration: Jane Smith");
        listModel.addElement("💰 Payment processed for order #1233");
        listModel.addElement("📊 Daily sales report generated");
        
        JList<String> activityList = new JList<>(listModel);
        activityList.setFont(new Font("Arial", Font.PLAIN, 12));
        activityList.setBackground(new Color(248, 249, 250));
        
        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setPreferredSize(new Dimension(0, 100));
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        return activityPanel;
    }
    
    private void startClock() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();
        updateTime(); // Initial update
    }
    
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - HH:mm:ss");
        timeLabel.setText(sdf.format(new Date()));
    }
    
    // Clean up timer when panel is no longer needed
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) {
            timer.stop();
        }
    }
}