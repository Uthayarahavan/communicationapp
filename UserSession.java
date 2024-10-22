import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSession {
    public static int currentUserId = 0; // Store the logged-in user's ID
    static String currentUsername = null; // Cache the username after it is retrieved

    // Method to get the username of the currently logged-in user
    public static String getCurrentUsername() {
        // If username is already cached, return it
        if (currentUsername != null) {
            return currentUsername;
        }

        // If no currentUserId is set, return null
        if (currentUserId == 0) {
            return null;
        }

        // Query the database to retrieve the username based on currentUserId
        String query = "SELECT username FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the user_id in the query
            pstmt.setInt(1, currentUserId);

            // Execute the query and get the result
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve the username from the result set
                currentUsername = rs.getString("username");
                return currentUsername;
            } else {
                // If no user is found with the currentUserId, return null
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Method to reset the session (on logout, for example)
    public static void reset() {
        currentUserId = 0;
        currentUsername = null;
    }
}
