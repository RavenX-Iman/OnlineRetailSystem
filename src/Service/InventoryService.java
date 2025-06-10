package Service;

import onlineretailsystem.InventoryTransactionDAO;
import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InventoryService {
    private final InventoryTransactionDAO dao;
    private final Connection connection;

    public InventoryService() {
        try {
            this.connection = DBConnection.getConnection();
            this.dao = new InventoryTransactionDAO(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error", e);
        }
    }

    public List<InventoryTransaction> getAllInventoryTransactions() {
        return dao.getAllTransactions();
    }

    public boolean addInventoryTransaction(InventoryTransaction transaction) {
        if (transaction == null || transaction.getQuantity() <= 0) {
            return false;
        }
        return dao.insert(transaction);
    }

    public InventoryTransaction getTransactionById(int transactionId) {
        return dao.getTransactionById(transactionId);
    }

    public boolean deleteInventoryTransaction(int transactionId) {
        return dao.delete(transactionId);
    }

    public void close() {
        DBConnection.closeConnection(connection);
    }
}