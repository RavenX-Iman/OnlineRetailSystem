package Service;

import onlineretailsystem.CustomerDAO;
import onlineretailsystem.DBConnection;
import onlineretailsystem.ModelClasses.Customer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private CustomerDAO dao;

    // Constructor that initializes CustomerDAO with a Connection
    public CustomerService() {
        try {
            Connection conn = DBConnection.getConnection();
            this.dao = new CustomerDAO(conn);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            throw new RuntimeException("Database connection error", e);
        }
    }

    public List<Customer> getAllCustomers() {
        return dao.getAllCustomers();
    }

    public boolean addCustomer(Customer customer) {
        return dao.insertCustomer(customer);
    }

    // Method to close the connection
    public void close() {
        // Since CustomerDAO holds the Connection, we need to close it
        // Note: CustomerDAO needs a method to close or expose the Connection
    }
}