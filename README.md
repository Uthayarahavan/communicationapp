# Java Swing Communication System

## Overview
This project is a simple communication system built using Java Swing and PostgreSQL. It allows users to sign up, log in, send messages to other registered users, and view their chat history with specific users. The system is implemented as a desktop application using NetBeans.

## Features
- **User Authentication**: Users can sign up or log in using a username and password.
- **Messaging System**: Users can compose and send messages to existing users.
- **Chat History**: Users can view their past conversations by selecting a contact.
- **PostgreSQL Database Integration**: All user data and messages are stored in a PostgreSQL database.

## Database Schema
### User Table (`users`)
| Column Name | Data Type | Description |
|-------------|----------|-------------|
| user_id     | SERIAL PRIMARY KEY | Unique identifier for each user |
| username    | VARCHAR(255) UNIQUE | Username for login |
| password    | VARCHAR(255) | Hashed password for security |

### Message Table (`messages`)
| Column Name | Data Type | Description |
|-------------|----------|-------------|
| message_id  | SERIAL PRIMARY KEY | Unique identifier for each message |
| sender_id   | INTEGER REFERENCES users(user_id) | ID of the sender |
| receiver_id | INTEGER REFERENCES users(user_id) | ID of the recipient |
| message     | TEXT | Content of the message |
| timestamp   | TIMESTAMP DEFAULT CURRENT_TIMESTAMP | Time the message was sent |

## Technologies Used
- **Java Swing**: GUI implementation
- **NetBeans IDE**: Development environment
- **JDBC (Java Database Connectivity)**: Database connection
- **PostgreSQL**: Database management

## Installation & Setup
1. Install PostgreSQL and create a database (e.g., `communication_db`).
2. Create the `users` and `messages` tables using the schema provided.
3. Clone this repository and open the project in NetBeans.
4. Update the `DatabaseConnection.java` file with your PostgreSQL connection details.
5. Run the application from NetBeans.

## Usage
1. **Sign Up/Login**: Register as a new user or log in with an existing account.
2. **Compose Message**: Click the "Compose" button to send a message to another registered user.
3. **View Chat History**: Click on a contact's name to view past messages with that user.

## Future Improvements
- Implement end-to-end encryption for messages.
- Add a user online status feature.
- Improve the UI with a modern framework.
- Implement group messaging.

## Author
- Developed by [Your Name]

## License
This project is open-source and available under the MIT License.

