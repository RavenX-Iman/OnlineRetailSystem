package onlineretailsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import onlineretailsystem.ModelClasses.InventoryTransaction;
import onlineretailsystem.ModelClasses.InventoryTransaction.TransactionType;
import onlineretailsystem.ModelClasses.Product;

public class InventoryTransactionDAO {
    private final Connection conn;

    // Constructor for shared connection
    public InventoryTransactionDAO(Connection conn) {
        this.conn = conn;
    }

    // Default constructor using DBConnection
    public InventoryTransactionDAO() {
        Connection tempConn = null;
        try {
            tempConn = DBConnection.getConnection();
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "initialize InventoryTransactionDAO");
        }
        this.conn = tempConn;
    }

    // Insert transaction
    public boolean insert(InventoryTransaction transaction) {
        String sql = "INSERT INTO InventoryTransactions (ProductID, TransactionType, Quantity, Reason, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getProduct().getProductId());
            stmt.setString(2, transaction.getTransactionType().name());
            stmt.setInt(3, transaction.getQuantity());
            stmt.setString(4, transaction.getReason());
            stmt.setTimestamp(5, Timestamp.valueOf(transaction.getCreatedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0)
                return false;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaction.setTransactionId(generatedKeys.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "insert inventory transaction");
            return false;
        }
    }

    // Get all transactions
    public List<InventoryTransaction> getAllTransactions() {
        List<InventoryTransaction> list = new ArrayList<>();
        String sql = "SELECT * FROM InventoryTransactions";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ProductDAO productDAO = new ProductDAO(conn);

            while (rs.next()) {
                InventoryTransaction t = new InventoryTransaction();
                t.setTransactionId(rs.getInt("TransactionID"));
                t.setProduct(productDAO.getProductById(rs.getInt("ProductID")));
                t.setTransactionType(TransactionType.valueOf(rs.getString("TransactionType")));
                t.setQuantity(rs.getInt("Quantity"));
                t.setReason(rs.getString("Reason"));
                t.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
                list.add(t);
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "fetch all inventory transactions");
        }
        return list;
    }

    // Get transaction by ID
    public InventoryTransaction getTransactionById(int id) {
        String sql = "SELECT * FROM InventoryTransactions WHERE TransactionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            ProductDAO productDAO = new ProductDAO(conn);

            if (rs.next()) {
                InventoryTransaction t = new InventoryTransaction();
                t.setTransactionId(rs.getInt("TransactionID"));
                t.setProduct(productDAO.getProductById(rs.getInt("ProductID")));
                t.setTransactionType(TransactionType.valueOf(rs.getString("TransactionType")));
                t.setQuantity(rs.getInt("Quantity"));
                t.setReason(rs.getString("Reason"));
                t.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
                return t;
            }
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "get inventory transaction by ID");
        }
        return null;
    }

    // Update transaction
    public boolean update(InventoryTransaction t) {
        String sql = "UPDATE InventoryTransactions SET ProductID = ?, TransactionType = ?, Quantity = ?, Reason = ?, CreatedAt = ? WHERE TransactionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getProduct().getProductId());
            stmt.setString(2, t.getTransactionType().name());
            stmt.setInt(3, t.getQuantity());
            stmt.setString(4, t.getReason());
            stmt.setTimestamp(5, Timestamp.valueOf(t.getCreatedAt()));
            stmt.setInt(6, t.getTransactionId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "update inventory transaction");
            return false;
        }
    }

    // Delete transaction
    public boolean delete(int id) {
        String sql = "DELETE FROM InventoryTransactions WHERE TransactionID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            DBErrorHandler.handle(e, "delete inventory transaction");
            return false;
        }
    }
}
