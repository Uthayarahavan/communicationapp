import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/communication_db"; // Replace with your database URL
    private static final String USER = "postgres"; // Replace with your PostgreSQL username
    private static final String PASSWORD = "0720"; // Replace with your PostgreSQL password


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
