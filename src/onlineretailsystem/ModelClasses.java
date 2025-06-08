package onlineretailsystem;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;



public class ModelClasses {

    public static class Customer {
        private int customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private LocalDateTime createdAt;
        private List<Order> orders;

        // Constructor
        public Customer(){

        }

        public Customer(String fn, String ln, String e, String p,String ad, String c, String s,String code, String cou) {
            this.firstName = fn;
            this.lastName =ln;
            this.email = e;
            this.phone = p;
            address = ad;
            city = c;
            state =s;
            postalCode = code;
            country = cou;
            this.orders = new ArrayList<>();
            this.createdAt = LocalDateTime.now();

        }

        // Getters and Setters
        public void setFirstName(String fn){
            firstName = fn;
        }
        String getFirstName(){
            return firstName;
        }

        public void setLastName(String ln){
            lastName = ln;
        }
        String getLastName(){
            return lastName;
        }

        public void setEmail(String e) {
            email = e;
        }
        String getEmail() {
            return email;
        }

        public void setPhone(String p) {
            phone = p;
        }
        String getPhone() {
            return phone;
        }

        public void setAddress(String ad) {
            address = ad;
        }
        String getAddress() {
            return address;
        }

        public void setCity(String c) {
            city = c;
        }
        String getCity() {
            return city;
        }

        public void setState(String s) {
            state = s;
        }
        String getState() {
            return state;
        }

        public void setPostalCode(String code) {
            postalCode = code;
        }
        String getPostalCode() {
            return postalCode;
        }

        public void setCountry(String cou) {
            country = cou;
        }
        String getCountry() {
            return country;
        }

        public void setCreatedAt(LocalDateTime ca) {
            createdAt = ca;
        }
        LocalDateTime getCreatedAt() {
            return createdAt;
        }
        public int getCustomerId() {
            return customerId;
        }
        void setCustomerId(int id) {
            this.customerId = id;
        }


        public List<Order> getOrders() {
            return orders; }
        public void addOrder(Order order) {
            this.orders.add(order); }

        @Override
        public String toString() {
            return "Customer{" +
                    "customerId=" + customerId +
                    ", name='" + getFirstName() + " " + getLastName() + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    public static class Category {
        private int categoryId;
        private String categoryName;
        private String description;
        private List<Product> products;

        public Category(int ci, String cn, String d) {
            categoryId = ci;
            categoryName = cn;
            description = d;
            products = new ArrayList<>();
        }

        // Getters and Setters
        public int getCategoryId() {
            return categoryId; }
        public void setCategoryId(int ci) {
            categoryId = ci; }

        public String getCategoryName() {
            return categoryName; }
        public void setCategoryName(String cn) {
            categoryName = cn; }

        public String getDescription() { return description; }
        public void setDescription(String d) { description = d; }

        public List<Product> getProducts() { return products; }
        public void addProduct(Product p) { this.products.add(p); }

        @Override
        public String toString() {
            return "Category{" +
                    "categoryId=" + categoryId +
                    ", categoryName='" + categoryName + '\'' +
                    ", productsCount=" + products.size() +
                    '}';
        }
    }

    public static class Product {
        private int productId;
        private String productName;
        private Category category;
        private BigDecimal price;
        private int stock;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime modifiedAt;

        public Product(String productName, Category category, BigDecimal price, int stock) {
            this.productId = 0; // default or auto-generated
            this.productName = productName;
            this.category = category;
            this.price = price;
            this.stock = stock;
            this.createdAt = LocalDateTime.now();
            this.modifiedAt = LocalDateTime.now();
            this.createdBy = "System"; // or get from current user
        }


        public Product(int productId, String productName, Category category, BigDecimal price, int stock,
                       LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt) {
            this.productId = productId;
            this.productName = productName;
            this.category = category;
            this.price = price;
            this.stock = stock;
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.modifiedAt = modifiedAt;
        }

        // Constructor with CreatedBy
//        public Product(String productName, Category category, BigDecimal price, int stock, String createdBy) {
//            this(productName, category, price, stock);
//            this.createdBy = createdBy;
//        }


        // Getters and Setters
        public int getProductId() {
            return productId; }
        public void setProductId(int pi) { productId = pi; }

        public String getProductName() {
            return productName; }
        public void setProductName(String pn) {
            productName = pn;
            modifiedAt = LocalDateTime.now();
        }

        public Category getCategory() {
            return category; }
        public void setCategory(Category c) { category = c; }

        public BigDecimal getPrice() {
            return price; }
        public void setPrice(BigDecimal p) {
            price = p;
            modifiedAt = LocalDateTime.now();
        }

        public int getStock() {
            return stock; }
        public void setStock(int s) {
            stock = s;
            modifiedAt = LocalDateTime.now();
        }

        public boolean isInStock() { return stock > 0; }

        public void reduceStock(int quantity) {
            if (quantity <= stock) {
                stock -= quantity;
                modifiedAt = LocalDateTime.now();
            } else {
                throw new IllegalArgumentException("Insufficient stock");
            }
        }

        public void addStock(int quantity) {
            this.stock += quantity;
            this.modifiedAt = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return "Product{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", price=" + price +
                    ", stock=" + stock +
                    '}';
        }
    }

    public static class Order {
        public enum OrderStatus {
            PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
        }

        private int orderId;
        private Customer customer;
        private LocalDateTime orderDate;
        private BigDecimal totalAmount;
        private OrderStatus status;
        private List<OrderItem> orderItems;
        private Payment payment;

        public Order(){

        }

        public Order(Customer c) {
            customer = c;
            orderDate = LocalDateTime.now();
            status = OrderStatus.PENDING;
            orderItems = new ArrayList<>();
            totalAmount = BigDecimal.ZERO;
        }

        // Getters and Setters
        public int getOrderId() {
            return orderId; }
        public void setOrderId(int oi) {
            this.orderId = oi; }

        public Customer setCustomer(Customer c){
            customer = c;
            return c;
        }
        public Customer getCustomer() {
            return customer; }

        public void setOrderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
        }
        public LocalDateTime getOrderDate() {
            return orderDate; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal t) {
            totalAmount = t; }

        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus s) { status = s; }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
        public List<OrderItem> getOrderItems() { return orderItems; }

        public void addOrderItem(OrderItem item) {
            this.orderItems.add(item);
            calculateTotalAmount();
        }

        public void removeOrderItem(OrderItem item) {
            orderItems.remove(item);
            calculateTotalAmount();
        }

        private void calculateTotalAmount() {
            totalAmount = orderItems.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public Payment getPayment() { return payment; }
        public void setPayment(Payment payment) { this.payment = payment; }

        @Override
        public String toString() {
            return "Order{" + "orderId=" + orderId + ", customer=" + customer.getFirstName()+" " + customer.getLastName() + ", totalAmount=" + totalAmount +
                    ", status=" + status + '}';
        }
    }
    public static class OrderItem {
        private int orderItemId;
        private Order order;
        private Product productName;
        private int quantity;
        private BigDecimal price;

        public OrderItem(){

        }

        public OrderItem(Order o, Product p, int q) {
            order = o;
            productName = p;
            quantity = q;
            price = productName.getPrice(); // Capture price at time of order
        }

        // Getters and Setters

        public void setOrderItemId(int id) {
            orderItemId = id; }
        public int getOrderItemId() {
            return orderItemId; }

        public Order getOrder() { return order; }

        public void setProductName(String p) {
            productName = productName;
        }
        public Product getProductName() { return productName; }

        public void setQuantity(int q) { quantity = q; }
        public int getQuantity() { return quantity; }

        public void setPrice(BigDecimal p) { price = p; }
        public BigDecimal getPrice() { return price; }

        public BigDecimal getSubTotal() {
            return price.multiply(BigDecimal.valueOf(quantity));
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "product=" + productName.getProductName() +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    ", subtotal=" + getSubTotal() +
                    '}';
        }
    }

    public static class Payment {
        public enum PaymentMethod {
            CASH_ON_DELIVERY, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, JAZZCASH, EASYPAISA
        }

        private int paymentId;
        private Order order;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String transactionId;
        private LocalDateTime paymentDate;

        public Payment(){

        }

        public Payment(Order o, BigDecimal a, PaymentMethod pm) {
            order = o;
            amount = a;
            paymentMethod = pm;
            paymentDate = LocalDateTime.now();
        }

        // Setters and Getters using short-form
        public void setPaymentId(int id) { paymentId = id; }
        public int getPaymentId() { return paymentId; }

        public Order getOrder() { return order; }

        public BigDecimal getAmount() { return amount; }

        public PaymentMethod getPaymentMethod() { return paymentMethod; }

        public void setTransactionId(String tid) { transactionId = tid; }
        public String getTransactionId() { return transactionId; }

        public LocalDateTime getPaymentDate() { return paymentDate; }

        @Override
        public String toString() {
            return "Payment{" +
                    "paymentId=" + paymentId +
                    ", amount=" + amount +
                    ", method=" + paymentMethod +
                    ", transactionId='" + transactionId + '\'' +
                    '}';
        }
    }

    public static class InventoryTransaction {
        public enum TransactionType {
            RESTOCK, SALE, ADJUSTMENT
        }

        private int transactionId;
        private Product product;
        private TransactionType transactionType;
        private int quantity;
        private String reason;
        private LocalDateTime createdAt;

        public InventoryTransaction(){

        }

        public InventoryTransaction(Product p, TransactionType t, int q, String r) {
            this.product = p;
            this.transactionType = t;
            this.quantity = q;
            this.reason = r;
            this.createdAt = LocalDateTime.now();
        }

        // Getters and Setters
        public void setProduct(Product product) {
            this.product = product;
        }

        public void setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public int getTransactionId() { return transactionId; }
        public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

        public Product getProduct() { return product; }
        public TransactionType getTransactionType() { return transactionType; }
        public int getQuantity() { return quantity; }
        public String getReason() { return reason; }
        public LocalDateTime getCreatedAt() { return createdAt; }

        @Override
        public String toString() {
            return "InventoryTransaction{" +
                    "transactionId=" + transactionId +
                    ", product=" + product.getProductName() +
                    ", type=" + transactionType +
                    ", quantity=" + quantity +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

}
