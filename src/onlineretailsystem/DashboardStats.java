package onlineretailsystem;

/**
 * DashboardStats class represents the statistics displayed on the dashboard.
 * It includes various metrics such as total customers, products, pending orders,
 * total revenue, new customers today, new products this week, revenue growth percentage,
 * out-of-stock products, and low-stock products.
 */
public class DashboardStats {
    private int totalCustomers;
    private int totalProducts;
    private int pendingOrders;
    private double totalRevenue;
    private int newCustomersToday;
    private int newProductsThisWeek;
    private double revenueGrowthPercentage;
    private int outOfStockProducts;
    private int lowStockProducts;
    
    // Constructors
    public DashboardStats() {}
    
    public DashboardStats(int totalCustomers, int totalProducts, int pendingOrders, 
                         double totalRevenue, int newCustomersToday, int newProductsThisWeek,
                         double revenueGrowthPercentage, int outOfStockProducts, int lowStockProducts) {
        this.totalCustomers = totalCustomers;
        this.totalProducts = totalProducts;
        this.pendingOrders = pendingOrders;
        this.totalRevenue = totalRevenue;
        this.newCustomersToday = newCustomersToday;
        this.newProductsThisWeek = newProductsThisWeek;
        this.revenueGrowthPercentage = revenueGrowthPercentage;
        this.outOfStockProducts = outOfStockProducts;
        this.lowStockProducts = lowStockProducts;
    }
    
    // Getters and Setters
    public int getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(int totalCustomers) { this.totalCustomers = totalCustomers; }
    
    public int getTotalProducts() { return totalProducts; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }
    
    public int getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }
    
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public int getNewCustomersToday() { return newCustomersToday; }
    public void setNewCustomersToday(int newCustomersToday) { this.newCustomersToday = newCustomersToday; }
    
    public int getNewProductsThisWeek() { return newProductsThisWeek; }
    public void setNewProductsThisWeek(int newProductsThisWeek) { this.newProductsThisWeek = newProductsThisWeek; }
    
    public double getRevenueGrowthPercentage() { return revenueGrowthPercentage; }
    public void setRevenueGrowthPercentage(double revenueGrowthPercentage) { this.revenueGrowthPercentage = revenueGrowthPercentage; }
    
    public int getOutOfStockProducts() { return outOfStockProducts; }
    public void setOutOfStockProducts(int outOfStockProducts) { this.outOfStockProducts = outOfStockProducts; }
    
    public int getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(int lowStockProducts) { this.lowStockProducts = lowStockProducts; }
}
