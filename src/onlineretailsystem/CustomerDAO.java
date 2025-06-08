package onlineretailsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import onlineretailsystem.ModelClasses.Customer;

public class CustomerDAO {

    // Insert a customer into the database
    public void insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer_table (FirstName, LastName, Email, Phone, Address, City, State, PostalCode, Country) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer_table";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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

                // Set ID and createdAt
                customer.setCustomerId(rs.getInt("CustomerID"));
                Timestamp timestamp = rs.getTimestamp("createdat");
                if (timestamp != null) {
                    customer.setCreatedAt(timestamp.toLocalDateTime());
                }

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }

        return customers;
    }

    // Get customer by ID
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer_table WHERE CustomerID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            System.out.println("Get by ID failed: " + e.getMessage());
        }
        return null;
    }

    // Update existing customer
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customer_table SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, " +
                     "Address = ?, City = ?, State = ?, PostalCode = ?, Country = ? WHERE CustomerID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Delete customer by ID
    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customer_table WHERE CustomerID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " customer(s).");

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}
