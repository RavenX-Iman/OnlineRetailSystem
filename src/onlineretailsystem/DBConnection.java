package onlineretailsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Updated URL with SSL settings for development
    private static final String URL = "jdbc:sqlserver://localhost:59598;Database=OnlineRetailDB;Trusted_Connection=True;trustServerCertificate=true;";
    private static final String USER = "retail_userIman";
    private static final String PASSWORD = "imanhuma157";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
            throw new SQLException("JDBC Driver not found", e);
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
        return connection;
    }
}