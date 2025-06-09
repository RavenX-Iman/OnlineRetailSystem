package onlineretailsystem;
import java.sql.SQLException;

public class DBErrorHandler {
    public static void handle(SQLException e, String actionContext) {
        String message = e.getMessage();
        
        if (message.contains("foreign key")) {
            System.out.println("Cannot " + actionContext + ": Related records exist.");
        } else if (message.contains("unique") || message.contains("duplicate")) {
            System.out.println("Duplicate data detected while trying to " + actionContext + ".");
        } else {
            System.out.println("Database error while trying to " + actionContext + ": " + message);
        }

        // Optional: Log full trace
        e.printStackTrace();
    }
}
