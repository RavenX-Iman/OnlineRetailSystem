package onlineretailsystem;//These all import statements are here to get data from different classes of a package
import gui.AdminPanel;
import gui.CustomerPanel;
import gui.DashboardPanel;
import gui.InventoryTransactionPanel;
import gui.ProductPanel;
import gui.PaymentPanel;
import gui.OrderPanel;
import gui.OrderItemPanel;

import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Online Retail System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Dashboard", new DashboardPanel());
        tabs.add("Customers", new CustomerPanel());
        tabs.add("Admin", new AdminPanel());
        tabs.addTab("Products", new ProductPanel());
        tabs.addTab("Orders", new OrderPanel());
        tabs.addTab("Order Items", new OrderItemPanel());
        tabs.addTab("Payments", new PaymentPanel());
        tabs.addTab("Inventory", new InventoryTransactionPanel());

        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Optional: Set modern theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();
    }
}
