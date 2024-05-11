import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = Database.getConnection();

            // Check if the database connection is successful
            if (connection != null) {
                System.out.println("Database connection successful.");

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Failed to connect to the database
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
