package onlineretailsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import onlineretailsystem.ModelClasses.Customer;

public class CustomerDAO {
    
    private static CustomerDAO instance;
    private final Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    public static CustomerDAO getInstance(Connection conn) {
        if (instance == null) {
            instance = new CustomerDAO(conn);
        }
        return instance;
    }
//    // Get customer by name
    // This method retrieves a customer based on their first name.
    // If multiple customers have the same first name, it returns the first one found.  
public ModelClasses.Customer getCustomerByName(String name) {
    String query = "SELECT * FROM Customer_table WHERE FirstName = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("CustomerID");
            String lastName = rs.getString("LastName");
            String email = rs.getString("Email");
            String phone = rs.getString("Phone");

            ModelClasses.Customer customer = new ModelClasses.Customer();
            customer.setCustomerId(id);
            customer.setFirstName(name);
            customer.setLastName(lastName);
            customer.setEmail(email);
            customer.setPhone(phone);

            return customer;
        }
    } catch (SQLException e) {
        DBErrorHandler.handle(e, "get customer by name");
    }
    return null;
}

    // Insert a customer into the database
    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer_table (FirstName, LastName, Email, Phone, Address, City, State, PostalCode, Country) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getCity());
            stmt.setString(7, customer.getState());
            stmt.setString(8, customer.getPostalCode());
            stmt.setString(9, customer.getCountry());

            int rowsInserted = stmt.executeUpdate();
            System.out.println("Inserted " + rowsInserted + " customer(s).");
            return rowsInserted > 0;

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert customer");
            return false;
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer_table";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("Phone"),
                    rs.getString("Address"),
                    rs.getString("City"),
                    rs.getString("State"),
                    rs.getString("PostalCode"),
                    rs.getString("Country")
                );

                customer.setCustomerId(rs.getInt("CustomerID"));
                Timestamp timestamp = rs.getTimestamp("createdat");
                if (timestamp != null) {
                    customer.setCreatedAt(timestamp.toLocalDateTime());
                }

                customers.add(customer);
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch all customers");
        }

        return customers;
    }

    // Get customer by ID
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer_table WHERE CustomerID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Address"),
                        rs.getString("City"),
                        rs.getString("State"),
                        rs.getString("PostalCode"),
                        rs.getString("Country")
                    );
                    customer.setCustomerId(rs.getInt("CustomerID"));
                    Timestamp timestamp = rs.getTimestamp("createdat");
                    if (timestamp != null) {
                        customer.setCreatedAt(timestamp.toLocalDateTime());
                    }
                    return customer;
                }
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "get customer by ID");
        }

        return null;
    }

    // Update existing customer
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer_table SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, " +
                     "Address = ?, City = ?, State = ?, PostalCode = ?, Country = ? WHERE CustomerID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getCity());
            stmt.setString(7, customer.getState());
            stmt.setString(8, customer.getPostalCode());
            stmt.setString(9, customer.getCountry());
            stmt.setInt(10, customer.getCustomerId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Updated " + rowsUpdated + " customer(s).");

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update customer");
        }
    }

    // Delete customer by ID
    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customer_table WHERE CustomerID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Deleted " + rowsDeleted + " customer(s).");
            } else {
                System.out.println("No customer found with ID: " + customerId);
            }

        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete customer");
        }
    }
    

    // Method to close the database connection
    public void closeConnection() {
        DBConnection.closeConnection(conn);
    }
}