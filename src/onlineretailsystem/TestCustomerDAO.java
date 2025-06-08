package onlineretailsystem;

import java.util.List;
import onlineretailsystem.ModelClasses.Customer;

public class TestCustomerDAO {
    public static void main(String[] args) {
        CustomerDAO dao = new CustomerDAO();

        // 1. Insert new customer
        Customer newCustomer = new Customer(
            "John", "Doe", "john.doe@example.com", "0300-1234567",
            "123 Street", "Lahore", "Punjab", "54000", "Pakistan"
        );
        dao.insertCustomer(newCustomer);

        // 2. Get all customers and print
        System.out.println("All customers:");
        List<Customer> customers = dao.getAllCustomers();
        for (Customer c : customers) {
            System.out.println(c);
        }

        // 3. Get customer by ID
        int testId = 21; // change this ID as needed
        Customer cById = dao.getCustomerById(testId);
        System.out.println("\nCustomer with ID " + testId + ":");
        if (cById != null) {
            System.out.println(cById);
        } else {
            System.out.println("No customer found.");
        }

        // 4. Update customer by ID
        if (cById != null) {
            cById.setCity("Islamabad");
            cById.setEmail("updated.email@example.com");
            dao.updateCustomer(cById);
            System.out.println("\nAfter update:");
            System.out.println(dao.getCustomerById(testId));
        }

        // 5. Delete customer by ID
        // dao.deleteCustomer(testId);
        // System.out.println("\nAfter deletion, all customers:");
        // List<Customer> afterDelete = dao.getAllCustomers();
        // for (Customer c : afterDelete) {
        //     System.out.println(c);

    }
}

