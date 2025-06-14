package gui;

import onlineretailsystem.DatabaseManager;
import onlineretailsystem.DashboardStats;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardPanel extends JPanel {
    private JLabel timeLabel;
    private Timer timer;
    private DashboardStats stats;
    private JPanel statsPanel;
    private JList<String> activityList;
    private DefaultListModel<String> listModel;
    private DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private DecimalFormat numberFormat = new DecimalFormat("#,##0");
    
    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(ModernColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Load data from database
        loadDashboardData();
        
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooterSection(), BorderLayout.SOUTH);
        
        startClock();
        
        // REMOVED: Auto-refresh timer that was causing popup issues
        // Auto-refresh every 30 seconds (SILENTLY in background)
        Timer refreshTimer = new Timer(30000, e -> silentRefreshDashboard());
        refreshTimer.start();
    }
    
    private void loadDashboardData() {
        // Load all dashboard statistics from database
        try {
            System.out.println("Loading dashboard data from database...");
            
            stats = new DashboardStats(
                DatabaseManager.getTotalCustomers(),
                DatabaseManager.getTotalProducts(),
                DatabaseManager.getPendingOrders(),
                DatabaseManager.getTotalRevenue(),
                DatabaseManager.getNewCustomersToday(),
                DatabaseManager.getNewProductsThisWeek(),
                DatabaseManager.getRevenueGrowthPercentage(),
                DatabaseManager.getOutOfStockProducts(),
                DatabaseManager.getLowStockProducts()
            );
            
            System.out.println("Data loaded successfully:");
            System.out.println("Total Customers: " + stats.getTotalCustomers());
            System.out.println("Total Products: " + stats.getTotalProducts());
            System.out.println("Total Revenue: " + stats.getTotalRevenue());
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to default values if database connection fails
            stats = new DashboardStats(0, 0, 0, 0.0, 0, 0, 0.0, 0, 0);
            
            // Show error only once, not repeatedly
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, 
                    "Failed to load dashboard data: " + e.getMessage() + 
                    "\n\nPlease check:\n" +
                    "1. Database connection\n" +
                    "2. SQL Server is running\n" +
                    "3. Database tables exist", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            });
        }
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ModernColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ModernColors.PRIMARY);
        JLabel title = new JLabel("🏪 CYANITY Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.NORTH);
        
        JLabel subtitle = new JLabel("Real-time insights from your OnlineRetailDB database");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 200, 255));
        subtitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Refresh button and time display
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(ModernColors.PRIMARY);
        
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setForeground(ModernColors.PRIMARY);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> manualRefreshDashboard()); // Changed to manual refresh
        rightPanel.add(refreshButton, BorderLayout.NORTH);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        rightPanel.add(timeLabel, BorderLayout.SOUTH);
        
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(ModernColors.BACKGROUND);
        
        // Statistics cards
        statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(ModernColors.BACKGROUND);
        
        updateStatCards();
        
        main.add(statsPanel, BorderLayout.NORTH);
        
        // Quick actions
        JPanel actionsPanel = createQuickActionsPanel();
        main.add(actionsPanel, BorderLayout.CENTER);
        
        return main;
    }
    
    private void updateStatCards() {
        statsPanel.removeAll();
        
        if (stats != null) {
            // Calculate alert levels
            String customerSubtitle = stats.getNewCustomersToday() > 0 ? 
                "+" + stats.getNewCustomersToday() + " today" : "No new customers today";
            
            String productSubtitle = stats.getNewProductsThisWeek() > 0 ? 
                "+" + stats.getNewProductsThisWeek() + " this week" : "No new products this week";
            
            String orderSubtitle = stats.getPendingOrders() > 0 ? 
                "Needs attention" : "All orders processed";
            
            String revenueSubtitle = String.format("%.1f%% this month", stats.getRevenueGrowthPercentage());
            
            // Determine colors based on conditions
            Color orderColor = stats.getPendingOrders() > 10 ? Color.MAGENTA : Color.BLUE;
            Color revenueColor = stats.getRevenueGrowthPercentage() >= 0 ? Color.BLUE : Color.MAGENTA;
            
            statsPanel.add(createStatCard("👥 Total Customers", 
                numberFormat.format(stats.getTotalCustomers()), 
                customerSubtitle, 
                ModernColors.SUCCESS));
                
            statsPanel.add(createStatCard("📦 Total Products", 
                numberFormat.format(stats.getTotalProducts()), 
                productSubtitle, 
                ModernColors.INFO));
                
            statsPanel.add(createStatCard("⏳ Pending Orders", 
                numberFormat.format(stats.getPendingOrders()), 
                orderSubtitle, 
                orderColor));
                
            statsPanel.add(createStatCard("💰 Total Revenue", 
                "$" + currencyFormat.format(stats.getTotalRevenue()), 
                revenueSubtitle, 
                revenueColor));
        } else {
            // Loading state
            for (int i = 0; i < 4; i++) {
                statsPanel.add(createStatCard("Loading...", "---", "Please wait", ModernColors.TEXT_SECONDARY));
            }
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
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
        listModel = new DefaultListModel<>();
        updateRecentActivities();
        
        activityList = new JList<>(listModel);
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
    
    private void updateRecentActivities() {
        try {
            List<String> activities = DatabaseManager.getRecentActivities();
            listModel.clear();
            
            if (activities.isEmpty()) {
                listModel.addElement("📊 No recent activities found");
            } else {
                for (String activity : activities) {
                    listModel.addElement(activity);
                }
            }
        } catch (Exception e) {
            listModel.clear();
            listModel.addElement("❌ Error loading activities: " + e.getMessage());
            System.err.println("Error loading activities: " + e.getMessage());
        }
    }
    
    // SILENT refresh for auto-refresh timer (no popup)
    private void silentRefreshDashboard() {
        try {
            System.out.println("Auto-refreshing dashboard data...");
            loadDashboardData();
            SwingUtilities.invokeLater(() -> {
                updateStatCards();
                updateRecentActivities();
            });
        } catch (Exception e) {
            System.err.println("Silent refresh failed: " + e.getMessage());
        }
    }
    
    // MANUAL refresh for button click (with user feedback)
    private void manualRefreshDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Show brief loading message
                JOptionPane.showMessageDialog(this, 
                    "Refreshing dashboard data...", 
                    "Refresh", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload data
                loadDashboardData();
                updateStatCards();
                updateRecentActivities();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Failed to refresh dashboard: " + e.getMessage(), 
                    "Refresh Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
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