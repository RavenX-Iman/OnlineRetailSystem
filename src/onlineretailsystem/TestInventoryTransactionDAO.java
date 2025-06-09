package onlineretailsystem;

import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.ModelClasses.InventoryTransaction.TransactionType;
import onlineretailsystem.ModelClasses.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TestInventoryTransactionDAO {
    public static void main(String[] args) {
        try {
            // Step 1: Establish shared DB connection
            Connection conn = DBConnection.getConnection();

            // Step 2: Instantiate DAO classes
            InventoryTransactionDAO transactionDAO = new InventoryTransactionDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);

            // Step 3: Fetch a product (assumes ID 1 exists)
            Product product = productDAO.getProductById(1);
            if (product == null) {
                System.out.println("Product with ID 1 not found. Insert a product before testing.");
                return;
            }

            // Step 4: Insert a new RESTOCK transaction
            InventoryTransaction transaction = new InventoryTransaction(
                product,
                TransactionType.RESTOCK,
                10,
                "Test restock via DAO"
            );
            transaction.setCreatedAt(LocalDateTime.now());

            boolean insertSuccess = transactionDAO.insert(transaction);
            System.out.println("Insert successful: " + insertSuccess + " | Transaction ID: " + transaction.getTransactionId());

            // Step 5: Fetch all transactions
            List<InventoryTransaction> allTransactions = transactionDAO.getAllTransactions();
            System.out.println("\nAll Transactions:");
            for (InventoryTransaction t : allTransactions) {
                System.out.println(t);
            }

            // Step 6: Get a transaction by ID
            int testId = transaction.getTransactionId();
            InventoryTransaction fetched = transactionDAO.getTransactionById(testId);
            System.out.println("\nFetched by ID: " + fetched);

            // Step 7: Update the transaction
            fetched.setQuantity(20);
            fetched.setReason("Updated reason after insert");
            boolean updateSuccess = transactionDAO.update(fetched);
            System.out.println("Update successful: " + updateSuccess);

            // Step 8: Fetch again to confirm update
            InventoryTransaction updated = transactionDAO.getTransactionById(testId);
            System.out.println("After update: " + updated);

            // Step 9: Delete the transaction
            boolean deleteSuccess = transactionDAO.delete(testId);
            System.out.println("Delete successful: " + deleteSuccess);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}

