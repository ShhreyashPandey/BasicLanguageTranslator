import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;

    private Connection connection;

    private String username; // Field for storing username
    private String password; // Field for storing password

    public LoginForm(JFrame parent) throws SQLException {
        setTitle("Login");
        setSize(300, 150);
        setLocationRelativeTo(parent);
        setModal(true);

        // Initialize database connection
        connection = Database.getConnection();

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText(); // Assigning username from the field
                password = new String(passwordField.getPassword()); // Assigning password from the field
                try {
                    if (authenticate()) {
                        JOptionPane.showMessageDialog(LoginForm.this, "Login Successful!");
                        dispose(); // Closing the login form after successful login
                    } else {
                        throw new AuthenticationException("Invalid username or password");
                    }
                } catch (AuthenticationException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginForm.this, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText(); // Assigning username from the field
                password = new String(passwordField.getPassword()); // Assigning password from the field
                try {
                    if (signUp()) {
                        JOptionPane.showMessageDialog(LoginForm.this, "Sign Up Successful!");
                    } else {
                        JOptionPane.showMessageDialog(LoginForm.this, "Sign Up Failed. Please try again.", "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (AuthenticationException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginForm.this, ex.getMessage(), "Sign Up Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        add(panel);
        setVisible(true);
    }

    protected boolean authenticate() throws AuthenticationException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // If a record is found, authentication is successful
                return true;
            } else {
                // If no matching record is found, authentication fails
                throw new AuthenticationException("Invalid username or password");
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            throw new AuthenticationException("Authentication failed: " + e.getMessage());
        }
    }


    private boolean signUp() throws AuthenticationException {
        // Check if the username is already taken
        String checkQuery = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, username);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                throw new AuthenticationException("Username already exists. Please choose a different one.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException("Sign-up failed: " + e.getMessage());
        }

        // If the username is available, proceed with sign-up
        String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                return true; // Sign-up successful
            } else {
                throw new AuthenticationException("Sign-up failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AuthenticationException("Sign-up failed: " + e.getMessage());
        }
    }


    public static void main(String[] args) throws SQLException {
        new LoginForm(null);
    }
}
