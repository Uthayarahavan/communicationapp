import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Custom TableModel to prevent editing
class NonEditableTableModel extends DefaultTableModel {
    // Constructor that takes column names and row count
    public NonEditableTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // All cells are non-editable
    }
}

public class MessagingForm extends JFrame {

    private JLabel loggedInUserLabel;
    private JButton composeMessageButton;
    private JButton logoutButton;
    private JTable previousMessagesTable;
    private NonEditableTableModel tableModel; // Use custom model

    public MessagingForm() {
        initComponents();
        displayLoggedInUser();
        loadPreviousMessages();
    }

    private void displayLoggedInUser() {
        String username = UserSession.getCurrentUsername();
        loggedInUserLabel.setText("Logged in as: " + (username != null ? username : "Unknown User"));
    }

    private void initComponents() {
        setTitle("Messaging Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // User Label
        loggedInUserLabel = new JLabel("Logged in as: ");
        add(loggedInUserLabel);

        // Compose Message Button
        composeMessageButton = new JButton("Compose Message");
        composeMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSendMessageForm();
            }
        });
        add(composeMessageButton);

        // Previous Messages Label
        JLabel previousMessagesLabel = new JLabel("Previous Messages:");
        add(previousMessagesLabel);

        // Table to display previous messages
        tableModel = new NonEditableTableModel(new String[]{"Previous Users"}, 0);
        previousMessagesTable = new JTable(tableModel);
        previousMessagesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        previousMessagesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openMessageHistory();
            }
        });
        JScrollPane scrollPane = new JScrollPane(previousMessagesTable);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        add(scrollPane);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        add(logoutButton);

        // Set the frame size and make it visible
        setSize(300, 300);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void loadPreviousMessages() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT u.username FROM messages m " +
                           "JOIN users u ON m.receiver_id = u.user_id " +
                           "WHERE m.sender_id = ?"; 
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, UserSession.currentUserId);
           
            
            ResultSet rs = pstmt.executeQuery();

            // Clear existing data in the table model
            tableModel.setRowCount(0);

            // Populate the table with previous usernames
            while (rs.next()) {
                String username = rs.getString("username");
                tableModel.addRow(new Object[]{username});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading previous messages.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSendMessageForm() {
        SwingUtilities.invokeLater(() -> {
            new SendMessage().setVisible(true); // Open SendMessage form
            // Uncomment this line if you want to close the MessagingForm
            // dispose(); // Close MessagingForm if desired
        });
    }

    private void openMessageHistory() {
        int selectedRow = previousMessagesTable.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
        String selectedUsername = (String) tableModel.getValueAt(selectedRow, 0);
        MessageHistory messageHistory = new MessageHistory(selectedUsername); // Create MessageHistory instance
        messageHistory.setVisible(true);// Open MessageHistory for selected user
            // Do not dispose of MessagingForm to keep it open
        }
    }

    private void logout() {
        UserSession.reset(); // Reset user session
        new LoginForm().setVisible(true); // Open login page
        dispose(); // Close MessagingForm
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MessagingForm());
    }
}
