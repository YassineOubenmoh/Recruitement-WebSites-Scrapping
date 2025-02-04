package projet_scrapping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ScrapingAppWithInterface extends JFrame {
    private final JPanel welcomePanel;
    private final JButton startScrapingButton;
    private final JButton rekruteButton;
    private final JButton annoncesButton;
    private final JButton myjobButton; // Added "myjob" button
    private final JTextArea outputTextArea;

    public ScrapingAppWithInterface() {
        super("Web Scraping App");

        // Set Nimbus Look and Feel for a modern appearance
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Welcome Panel
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        // Centered Welcome Message
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center; font-size: 18px; color: #ffffff;'>Welcome to <b>Scraping App</b>!</div></html>");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(20));

        // Start Scraping Button
        startScrapingButton = new JButton("Emploi");
        styleButton(startScrapingButton);

        // Rekrute Button
        rekruteButton = new JButton("Rekrute");
        styleButton(rekruteButton);

        // Annonces Button
        annoncesButton = new JButton("Maroc Annonces");
        styleButton(annoncesButton);

        // myjob Button
        myjobButton = new JButton("My Job"); // Added "myjob" button
        styleButton(myjobButton);

        // Center buttons
        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(startScrapingButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(rekruteButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(annoncesButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(myjobButton); // Added "myjob" button
        buttonBox.add(Box.createHorizontalGlue());

        // Add buttons to the layout
        welcomePanel.add(buttonBox);

        // Output text area
        outputTextArea = new JTextArea(20, 50);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        // Set layout
        setLayout(new BorderLayout());
        add(welcomePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Attach event listeners
        startScrapingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startScraping();
            }
        });

        rekruteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRekruteScraping();
            }
        });

        annoncesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAnnoncesScraping();
            }
        });

        myjobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startMyJobScraping(); // Added action for "myjob" button
            }
        });

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the frame
        getContentPane().setBackground(new Color(44, 62, 80)); // Set background color
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40)); // Set button size
    }

    private void startScraping() {
        loadDataFromDatabase("emploi");
    }

    private void startRekruteScraping() {
        loadDataFromDatabase("rekrute");
    }

    private void startAnnoncesScraping() {
        loadDataFromDatabase("annonces");
    }

    private void startMyJobScraping() {
        loadDataFromDatabase("myjob"); // Replace "myjob" with the actual table name
    }

    private void loadDataFromDatabase(String rekrute) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            String selectQuery = "SELECT * FROM " + rekrute;

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    StringBuilder data = new StringBuilder();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnValue = resultSet.getString(i);

                            data.append(columnName).append(": ").append(columnValue).append("\n");
                        }
                        data.append("\n");
                    }

                    // Display the data in the text area
                    outputTextArea.setText(data.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    private void loadDataFromDatabase2(String annonces) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            String selectQuery = "SELECT * FROM " + annonces;

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    StringBuilder data = new StringBuilder();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnValue = resultSet.getString(i);

                            data.append(columnName).append(": ").append(columnValue).append("\n");
                        }
                        data.append("\n");
                    }

                    // Display the data in the text area
                    outputTextArea.setText(data.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void loadDataFromDatabase1(String emploi) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            String selectQuery = "SELECT * FROM " + emploi;

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    StringBuilder data = new StringBuilder();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnValue = resultSet.getString(i);

                            data.append(columnName).append(": ").append(columnValue).append("\n");
                        }
                        data.append("\n");
                    }

                    // Display the data in the text area
                    outputTextArea.setText(data.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void loadDataFromDatabase3(String myjob) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String user = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password)) {
            String selectQuery = "SELECT * FROM " + myjob;

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    StringBuilder data = new StringBuilder();

                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnValue = resultSet.getString(i);

                            data.append(columnName).append(": ").append(columnValue).append("\n");
                        }
                        data.append("\n");
                    }

                    // Display the data in the text area
                    outputTextArea.setText(data.toString());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScrapingAppWithInterface scrapingApp = new ScrapingAppWithInterface();
            scrapingApp.setVisible(true);
        });
    }
}
