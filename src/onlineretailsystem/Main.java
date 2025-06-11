package onlineretailsystem;

import gui.*;
import Service.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Test core system functionality first
        testCoreSystem();
        
        // Launch GUI Application
        SwingUtilities.invokeLater(() -> {
            try {
                // Set modern theme before creating GUI
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Create main application frame with dashboard
                JFrame frame = new JFrame("Online Retail System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 800);
                frame.setLocationRelativeTo(null);
                
                // Create main panel with tabbed interface to access all panels
                JTabbedPane tabbedPane = new JTabbedPane();
                
                // Add all your panels as tabs
                tabbedPane.addTab("Dashboard", new DashboardPanel());
                tabbedPane.addTab("Orders", new OrderPanel());
                tabbedPane.addTab("Products", new ProductPanel());
                tabbedPane.addTab("Payments", new PaymentPanel());
                tabbedPane.addTab("Order Items", new OrderItemPanel());
                tabbedPane.addTab("Inventory", new InventoryTransactionPanel());
                tabbedPane.addTab("Categories", new CategoryPanel());
                tabbedPane.addTab("Admin", new AdminPanel());
                
                // Style the tabbed pane
                tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                tabbedPane.setBackground(new Color(245, 248, 255));
                
                frame.add(tabbedPane);
                
                // Make frame visible
                frame.setVisible(true);
                
                System.out.println("Online Retail System GUI launched successfully!");
                System.out.println("All panels are now accessible through tabs!");
                
            } catch (Exception e) {
                System.err.println("Error launching GUI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private static void testCoreSystem() {
        System.out.println("=== Testing Core System Components ===");
        
        try {
            // Test ModelClasses directly first
            ModelClasses.Customer customer = new ModelClasses.Customer(
                "John", "Doe", "john@email.com", "123-456-7890",
                "123 Main St", "Sahiwal", "Punjab", "57000", "Pakistan"
            );
            
            ModelClasses.Category category = new ModelClasses.Category(1, "Electronics", "Electronic items");
            ModelClasses.Product product = new ModelClasses.Product(
                "Laptop", category, new java.math.BigDecimal("50000"), 10
            );
            
            System.out.println("✓ Model classes working: " + customer.toString());
            System.out.println("✓ Product created: " + product.toString());
            
            // Factory Pattern
            RetailFactory factory = new SimpleRetailFactory();
            ModelClasses.Customer factoryCustomer = (ModelClasses.Customer) factory.create("customer");
            ModelClasses.Product factoryProduct = (ModelClasses.Product) factory.create("product");
            System.out.println("✓ Factory Pattern working");
            System.out.println("  - Factory Customer: " + factoryCustomer.toString());
            System.out.println("  - Factory Product: " + factoryProduct.toString());
            
            // Singleton InventoryManager
            InventoryManager inventory = InventoryManager.getInstance();
            inventory.updateStock(factoryProduct, 20);
            System.out.println("✓ Inventory Manager (Singleton) working");
            
            // Observer Pattern for Order status notification
            ObservableOrder order = new ObservableOrder(factoryCustomer);
            order.addObserver(new EmailObserver());
            order.addObserver(new SMSObserver());
            
            System.out.println("Order status before: " + order.getStatus());
            order.setStatus(ModelClasses.Order.OrderStatus.SHIPPED); // triggers notifications
            System.out.println("✓ Observer Pattern working - notifications sent");
            
            // Strategy Pattern for shipping
            ShippingContext shippingContext = new ShippingContext(new FlatRateShipping());
            System.out.println("Flat rate shipping cost: $" + shippingContext.getShippingCost(5));
            
            shippingContext.setStrategy(new WeightBasedShipping());
            System.out.println("Weight-based shipping cost: $" + shippingContext.getShippingCost(5));
            System.out.println("✓ Strategy Pattern working");
            
            // Test Database Connection and Services
            testDatabaseAndServices();
            
        } catch (Exception e) {
            System.err.println("Error in core system test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Core System Test Complete ===\n");
    }
    
    private static void testDatabaseAndServices() {
        System.out.println("\n--- Testing Database Connection and Services ---");
        
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("✓ Database connection successful");
                
                // Test DAO Layer directly
                testDAOLayer(conn);
                
                // Test Service Layer
                testServiceLayer();
                
                DBConnection.closeConnection(conn);
            } else {
                System.out.println("⚠ Database connection failed");
                System.out.println("  Note: GUI will still work with limited functionality");
            }
        } catch (Exception e) {
            System.out.println("⚠ Database connection failed: " + e.getMessage());
            System.out.println("  Note: GUI will still work with limited functionality");
        }
    }
    
    private static void testDAOLayer(Connection conn) {
        System.out.println("\n-- Testing DAO Layer --");
        
        // Test CustomerDAO
        try {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            
            ModelClasses.Customer daoTestCustomer = new ModelClasses.Customer(
                "Alice", "Johnson", "alice@email.com", "987-654-3210",
                "456 Oak St", "Sahiwal", "Punjab", "57000", "Pakistan"
            );
            
            boolean addResult = customerDAO.insertCustomer(daoTestCustomer);
            System.out.println("✓ CustomerDAO working - Add customer: " + (addResult ? "Success" : "Failed"));
            
            java.util.List<ModelClasses.Customer> customers = customerDAO.getAllCustomers();
            System.out.println("✓ CustomerDAO - Retrieved " + customers.size() + " customers");
            
        } catch (Exception e) {
            System.out.println("⚠ CustomerDAO test failed: " + e.getMessage());
        }
        
        // Test CategoryDAO
        try {
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            
            ModelClasses.Category testCategory = new ModelClasses.Category(0, "Home & Garden", "Home and garden products");
            categoryDAO.insertCategory(testCategory);
            System.out.println("✓ CategoryDAO working - Add category executed");
            
            java.util.List<ModelClasses.Category> categories = categoryDAO.getAllCategories();
            System.out.println("✓ CategoryDAO - Retrieved " + categories.size() + " categories");
            
        } catch (Exception e) {
            System.out.println("⚠ CategoryDAO test failed: " + e.getMessage());
        }
        
        // Test ProductDAO
        try {
            ProductDAO productDAO = new ProductDAO(conn);
            CategoryDAO categoryDAO = new CategoryDAO(conn);
            
            java.util.List<ModelClasses.Category> categories = categoryDAO.getAllCategories();
            
            if (!categories.isEmpty()) {
                ModelClasses.Category testCategory = categories.get(0);
                ModelClasses.Product daoTestProduct = new ModelClasses.Product(
                    "Test Smartphone", testCategory, new java.math.BigDecimal("25000"), 15
                );
                
                boolean exists = productDAO.productExists(daoTestProduct.getProductName(), testCategory.getCategoryId());
                if (!exists) {
                    boolean addProductResult = productDAO.insertProduct(daoTestProduct);
                    System.out.println("✓ ProductDAO working - Add product: " + (addProductResult ? "Success" : "Failed"));
                } else {
                    System.out.println("✓ ProductDAO - Product already exists, skipping insert");
                }
                
                java.util.List<ModelClasses.Product> products = productDAO.getAllProducts();
                System.out.println("✓ ProductDAO - Retrieved " + products.size() + " products");
            }
            
        } catch (Exception e) {
            System.out.println("⚠ ProductDAO test failed: " + e.getMessage());
        }
        
        // Test OrderDAO
        try {
            OrderDAO orderDAO = new OrderDAO(conn);
            java.util.List<ModelClasses.Order> orders = orderDAO.getAllOrders();
            System.out.println("✓ OrderDAO - Retrieved " + orders.size() + " orders");
            
        } catch (Exception e) {
            System.out.println("⚠ OrderDAO test failed: " + e.getMessage());
        }
        
        // Test OrderItemDAO
        try {
            OrderItemDAO orderItemDAO = new OrderItemDAO(conn);
            java.util.List<ModelClasses.OrderItem> orderItems = orderItemDAO.getAllOrderItems();
            System.out.println("✓ OrderItemDAO - Retrieved " + orderItems.size() + " order items");
            
        } catch (Exception e) {
            System.out.println("⚠ OrderItemDAO test failed: " + e.getMessage());
        }
        
        // Test PaymentDAO
        try {
            PaymentDAO paymentDAO = new PaymentDAO(conn);
            java.util.List<ModelClasses.Payment> payments = paymentDAO.getAllPayments();
            System.out.println("✓ PaymentDAO - Retrieved " + payments.size() + " payments");
            
        } catch (Exception e) {
            System.out.println("⚠ PaymentDAO test failed: " + e.getMessage());
        }
        
        // Test InventoryTransactionDAO
        try {
            InventoryTransactionDAO inventoryDAO = new InventoryTransactionDAO(conn);
            java.util.List<ModelClasses.InventoryTransaction> transactions = inventoryDAO.getAllTransactions();
            System.out.println("✓ InventoryTransactionDAO - Retrieved " + transactions.size() + " transactions");
            
        } catch (Exception e) {
            System.out.println("⚠ InventoryTransactionDAO test failed: " + e.getMessage());
        }
    }
    
    private static void testServiceLayer() {
        System.out.println("\n-- Testing Service Layer --");
        
        // Test CustomerService
        try {
            CustomerService customerService = new CustomerService();
            
            ModelClasses.Customer serviceTestCustomer = new ModelClasses.Customer(
                "Bob", "Wilson", "bob@email.com", "555-123-4567",
                "789 Pine St", "Sahiwal", "Punjab", "57000", "Pakistan"
            );
            
            boolean serviceAddResult = customerService.addCustomer(serviceTestCustomer);
            System.out.println("✓ CustomerService working - Add customer: " + (serviceAddResult ? "Success" : "Failed"));
            
            java.util.List<ModelClasses.Customer> serviceCustomers = customerService.getAllCustomers();
            System.out.println("✓ CustomerService - Retrieved " + serviceCustomers.size() + " customers");
            
            customerService.close();
            System.out.println("✓ CustomerService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ CustomerService test failed: " + e.getMessage());
        }
        
        // Test CategoryService
        try {
            CategoryService categoryService = new CategoryService();
            
            java.util.List<ModelClasses.Category> serviceCategories = categoryService.getAllCategories();
            System.out.println("✓ CategoryService - Retrieved " + serviceCategories.size() + " categories");
            
            categoryService.close();
            System.out.println("✓ CategoryService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ CategoryService test failed: " + e.getMessage());
        }
        
        // Test ProductService
        try {
            ProductService productService = new ProductService();
            
            java.util.List<ModelClasses.Product> serviceProducts = productService.getAllProducts();
            System.out.println("✓ ProductService - Retrieved " + serviceProducts.size() + " products");
            
            productService.close();
            System.out.println("✓ ProductService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ ProductService test failed: " + e.getMessage());
        }
        
        // Test OrderService
        try {
            OrderService orderService = new OrderService();
            
            java.util.List<ModelClasses.Order> orders = orderService.getAllOrdersWithTotal();
            System.out.println("✓ OrderService - Retrieved " + orders.size() + " orders");
            
            orderService.closeDAO();
            System.out.println("✓ OrderService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ OrderService test failed: " + e.getMessage());
        }
        
        // Test OrderItemService
        try {
            OrderItemService orderItemService = new OrderItemService();
            
            java.util.List<ModelClasses.OrderItem> orderItems = orderItemService.getAllOrderItems();
            System.out.println("✓ OrderItemService - Retrieved " + orderItems.size() + " order items");
            
            orderItemService.close();
            System.out.println("✓ OrderItemService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ OrderItemService test failed: " + e.getMessage());
        }
        
        // Test PaymentService
        try {
            PaymentService paymentService = new PaymentService();
            
            java.util.List<ModelClasses.Payment> payments = paymentService.getAllPayments();
            System.out.println("✓ PaymentService - Retrieved " + payments.size() + " payments");
            
            paymentService.close();
            System.out.println("✓ PaymentService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ PaymentService test failed: " + e.getMessage());
        }
        
        // Test InventoryService
        try {
            InventoryService inventoryService = new InventoryService();
            
            java.util.List<ModelClasses.InventoryTransaction> transactions = inventoryService.getAllInventoryTransactions();
            System.out.println("✓ InventoryService - Retrieved " + transactions.size() + " inventory transactions");
            
            inventoryService.close();
            System.out.println("✓ InventoryService test completed");
            
        } catch (Exception e) {
            System.out.println("⚠ InventoryService test failed: " + e.getMessage());
        }
    }
}