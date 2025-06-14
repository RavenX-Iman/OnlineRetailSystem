package onlineretailsystem;

import gui.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("Online Retail System - Cyanity");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();

        // Create a plain root card panel (not BackgroundPanel)
        cardPanel = new JPanel(cardLayout);

        // 1. Splash screen with fullscreen PNG
        SplashScreenPanel splashPanel = new SplashScreenPanel();

        // 2. Login screen inside a BackgroundPanel
        BackgroundPanel loginBackgroundPanel = new BackgroundPanel();
        loginBackgroundPanel.setLayout(new BorderLayout());
        loginBackgroundPanel.add(new LoginPanel(this), BorderLayout.CENTER);

        // 3. Admin and Customer Dashboards
        JPanel adminDashboard = createAdminDashboard();
        JPanel customerDashboard = createCustomerDashboard();

        // Add all views to root cardPanel
        cardPanel.add(splashPanel, "Splash");
        cardPanel.add(loginBackgroundPanel, "Login");
        cardPanel.add(adminDashboard, "AdminDashboard");
        cardPanel.add(customerDashboard, "CustomerDashboard");

        add(cardPanel);
        cardLayout.show(cardPanel, "Splash");

        // Switch to login after 5s
        Timer timer = new Timer(5000, e -> cardLayout.show(cardPanel, "Login"));
        timer.setRepeats(false);
        timer.start();

        setVisible(true);
    }

    public void showAdminDashboard() {
        cardLayout.show(cardPanel, "AdminDashboard");
    }

    public void showCustomerDashboard(String customerName) {
        cardLayout.show(cardPanel, "CustomerDashboard");
    }

    private JPanel createAdminDashboard() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Customers", new CustomerPanel());
        tabs.addTab("Admin", new AdminPanel());
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Orders", new OrderPanel());
        tabs.addTab("Order Items", new OrderItemPanel());
        tabs.addTab("Payments", new PaymentPanel());
        tabs.addTab("Inventory", new InventoryTransactionPanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCustomerDashboard() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Place Order", new OrderPanel());
        tabs.addTab("My Order Items", new OrderItemPanel());
        tabs.addTab("Payments", new PaymentPanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainFrame();
    }
}
