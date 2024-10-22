import javax.swing.*;
import java.sql.*;

public class MessageHistory extends JFrame {
    private int userId; // This should store the current user's ID
    private String selectedUser; // The username of the user whose messages are being viewed
    private JTextArea messageArea;

    public MessageHistory(String selectedUser) {
        this.userId = UserSession.currentUserId; // Assuming UserSession holds the logged-in user's ID
        this.selectedUser = selectedUser; // Set the selected username
        initComponents();
        loadMessages();
    }

    private void initComponents() {
        messageArea = new JTextArea();
        messageArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(messageArea); // Add a scroll pane for better usability
        add(scrollPane);
        setTitle("Message History with " + selectedUser);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
    }

    private void loadMessages() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Query to fetch messages between the current user and the selected user
            String query = "SELECT u.username AS sender_username, m.message, m.timestamp " +
                    "FROM messages m " +
                    "JOIN users u ON u.user_id = m.sender_id " +
                    "WHERE (m.sender_id = ? AND m.receiver_id = (SELECT user_id FROM users WHERE username = ?)) " +
                    "OR (m.receiver_id = ? AND m.sender_id = (SELECT user_id FROM users WHERE username = ?)) " +
                    "ORDER BY m.timestamp";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId); // Set the current user's ID as sender
            pstmt.setString(2, selectedUser); // Set the selected username as receiver
            pstmt.setInt(3, userId); // Set the current user's ID as receiver
            pstmt.setString(4, selectedUser); // Set the selected username as sender
            ResultSet rs = pstmt.executeQuery();

            messageArea.setText(""); // Clear existing text
            while (rs.next()) {
                String senderUsername = rs.getString("sender_username"); // Retrieve sender's username
                String message = rs.getString("message");
                String timestamp = rs.getString("timestamp");

                // Format message with sender's username and timestamp
                messageArea.append("[" + timestamp + "] " + senderUsername + ": " + message + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading messages.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
