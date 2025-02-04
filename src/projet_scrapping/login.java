package projet_scrapping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public login() {
        super("Login");

        // Set Nimbus Look and Feel for a modern appearance
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create components
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set preferred size

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0); // Add spacing
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(usernameLabel, gbc);

        gbc.gridy++;
        add(usernameField, gbc);

        gbc.gridy++;
        add(passwordLabel, gbc);

        gbc.gridy++;
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0); // Add spacing after password field
        add(loginButton, gbc);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void performLogin() {
        // Authentication logic here
        String enteredUsername = usernameField.getText();
        char[] enteredPasswordChars = passwordField.getPassword();
        String enteredPassword = new String(enteredPasswordChars);

        // Replace this with your authentication logic
        if (authenticate(enteredUsername, enteredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
         // Open the ScrapingAppWithInterface interface
            dispose(); // Close the login window
            SwingUtilities.invokeLater(() -> new ScrapingAppWithInterface().setVisible(true));
            // Open another interface or perform other actions
            dispose(); // Close the login window
            new ScrapingAppWithInterface(); // Open ScrapingAppWithInterface interface
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Please check your username and password.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear sensitive data
        Arrays.fill(enteredPasswordChars, '0');
        passwordField.setText("");
    }

    // Replace this with your actual authentication logic
    private boolean authenticate(String username, String password) {
        // Sample authentication logic (replace with your own)
        return username.equals("yassine") && password.equals("ensa2001");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new login();
        });
    }
}

