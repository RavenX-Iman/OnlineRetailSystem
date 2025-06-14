package onlineretailsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Updated connection string to match your working DBConnection.java
    private static final String CONNECTION_STRING = "jdbc:sqlserver://localhost:59598;databaseName=OnlineRetailDB;encrypt=true;trustServerCertificate=true";
    private static final String USER = "retail_userIman";
    private static final String PASSWORD = "imanhuma157";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
    }
    
    // Dashboard Statistics Methods
    public static int getTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM Customer_table";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total customers: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM Products_table";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total products: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getPendingOrders() {
        String sql = "SELECT COUNT(*) FROM Orders_table WHERE Status = 'Pending'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending orders: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public static double getTotalRevenue() {
        String sql = "SELECT SUM(TotalAmount) FROM Orders_table";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                double revenue = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : revenue; // Handle NULL case
            }
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // New customers today
    public static int getNewCustomersToday() {
        String sql = "SELECT COUNT(*) FROM Customer_table WHERE CAST(createdat AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting new customers today: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // New products this week
    public static int getNewProductsThisWeek() {
        String sql = "SELECT COUNT(*) FROM Products_table WHERE createdat >= DATEADD(DAY, -7, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting new products this week: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Revenue growth this month
    public static double getRevenueGrowthPercentage() {
        String sql = """
            WITH CurrentMonth AS (
                SELECT ISNULL(SUM(TotalAmount), 0) as CurrentRevenue
                FROM Orders_table 
                WHERE YEAR(Orderdate) = YEAR(GETDATE()) 
                AND MONTH(Orderdate) = MONTH(GETDATE())
            ),
            LastMonth AS (
                SELECT ISNULL(SUM(TotalAmount), 0) as LastRevenue
                FROM Orders_table 
                WHERE YEAR(Orderdate) = YEAR(DATEADD(MONTH, -1, GETDATE())) 
                AND MONTH(Orderdate) = MONTH(DATEADD(MONTH, -1, GETDATE()))
            )
            SELECT 
                CASE 
                    WHEN l.LastRevenue = 0 THEN 100.0
                    ELSE ((c.CurrentRevenue - l.LastRevenue) / l.LastRevenue) * 100
                END as GrowthPercentage
            FROM CurrentMonth c, LastMonth l
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting revenue growth: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    // Recent activities
    public static List<String> getRecentActivities() {
        List<String> activities = new ArrayList<>();
        
        // Recent orders
        String orderSql = """
            SELECT TOP 3 o.OrderID, c.FirstName, c.LastName, o.Orderdate, o.TotalAmount
            FROM Orders_table o
            JOIN Customer_table c ON o.CustomerID = c.CustomerID
            ORDER BY o.Orderdate DESC
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(orderSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String activity = String.format("🛒 New order #%d received from %s %s - $%.2f", 
                    rs.getInt("OrderID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getDouble("TotalAmount"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent orders: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Recent customers
        String customerSql = """
            SELECT TOP 2 FirstName, LastName, createdat
            FROM Customer_table
            ORDER BY createdat DESC
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(customerSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String activity = String.format("👤 New customer registration: %s %s", 
                    rs.getString("FirstName"),
                    rs.getString("LastName"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent customers: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Recent payments
        String paymentSql = """
            SELECT TOP 2 p.Amount, p.PaymentMethod, p.PaymentDate, p.OrderID
            FROM Payments_table p
            ORDER BY p.PaymentDate DESC
            """;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(paymentSql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String activity = String.format("💰 Payment of $%.2f processed for order #%d via %s", 
                    rs.getDouble("Amount"),
                    rs.getInt("OrderID"),
                    rs.getString("PaymentMethod"));
                activities.add(activity);
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent payments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return activities;
    }
    
    // Out of stock products
    public static int getOutOfStockProducts() {
        String sql = "SELECT COUNT(*) FROM Products_table WHERE Stock = 0";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting out of stock products: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Low stock products (stock <= 5)
    public static int getLowStockProducts() {
        String sql = "SELECT COUNT(*) FROM Products_table WHERE Stock <= 5 AND Stock > 0";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting low stock products: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Test connection method
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Database connection successful!");
            System.out.println("Connected to: " + conn.getMetaData().getURL());
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return false;
        }
    }
}