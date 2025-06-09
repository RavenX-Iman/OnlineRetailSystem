package onlineretailsystem;

import java.math.BigDecimal;
import java.util.List;

public class TestOrderDAO {
    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();

        System.out.println("Fetching all orders:");
        List<ModelClasses.Order> orders = dao.getAllOrders();
        for (ModelClasses.Order o : orders) {
            System.out.println(o);
        }

        System.out.println("\nInserting new order for customer ID 1...");
        CustomerDAO customerDAO = new CustomerDAO();
        ModelClasses.Customer c = customerDAO.getCustomerById(1);
        ModelClasses.Order newOrder = new ModelClasses.Order(c);
        newOrder.setTotalAmount(new BigDecimal("799.00"));
        dao.insertOrder(newOrder);

        System.out.println("\nFetching order by ID " + newOrder.getOrderId());
        ModelClasses.Order fetched = dao.getOrderById(newOrder.getOrderId());
        System.out.println(fetched);

        System.out.println("\nUpdating order total:");
        dao.updateOrderTotal(newOrder.getOrderId(), new BigDecimal("999.99"));
        ModelClasses.Order updated = dao.getOrderById(newOrder.getOrderId());
        System.out.println("Updated total: " + updated.getTotalAmount());

        System.out.println("\nAttempting delete:");
        dao.deleteOrder(newOrder.getOrderId());
    }
}
