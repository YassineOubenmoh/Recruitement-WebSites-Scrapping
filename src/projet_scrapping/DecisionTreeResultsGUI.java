package projet_scrapping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DecisionTreeResultsGUI extends JFrame {

    // Decision tree results and evaluation metrics
    private String decisionTreeResults = "=== Predictions on training set ===\n" +
            // ... (your decision tree results here) ...
            "327,14,14,0";

    private String evaluationMetrics = "=== Summary ===\n" +
            "Correlation coefficient                  0.9646\n" +
            "Mean absolute error                      0.4229\n" +
            "Root mean squared error                  1.3963\n" +
            "Relative absolute error                  9.3893 %\n" +
            "Root relative squared error             26.3837 %\n" +
            "Total Number of Instances              327";

    // Path to the decision tree image
    private String decisionTreeImagePath = "C:\\Users\\apple\\Desktop\\decisiontree.png";

    // JLabel to display the decision tree image
    private JLabel decisionTreeImageLabel;

    public DecisionTreeResultsGUI() {
        // Set frame properties
        setTitle("Decision Tree Results");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TextArea for decision tree results
        JTextArea resultTextArea = new JTextArea(decisionTreeResults);
        resultTextArea.setEditable(false);

        // Labels for evaluation metrics
        JLabel metricsLabel = new JLabel("Evaluation Metrics:");
        JLabel metricsTextArea = new JLabel(evaluationMetrics);

        // Button to trigger tree visualization
        JButton visualizeTreeButton = new JButton("Visualize Decision Tree");

        // ActionListener for the button
        visualizeTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load and display the decision tree image
                showDecisionTreeImage();
            }
        });

        // JScrollPane for decision tree results
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        // Initialize decisionTreeImageLabel
        decisionTreeImageLabel = new JLabel();

        // Layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(scrollPane, BorderLayout.CENTER);
        topPanel.add(visualizeTreeButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.CENTER);
        add(metricsLabel, BorderLayout.NORTH);
        add(metricsTextArea, BorderLayout.SOUTH);
        add(decisionTreeImageLabel, BorderLayout.EAST);
    }

    // Method to load and display the decision tree image
    private void showDecisionTreeImage() {
        try {
            // Load the image
            Image decisionTreeImage = ImageIO.read(new File(decisionTreeImagePath));
            
            // Set the image to the label
            decisionTreeImageLabel.setIcon(new ImageIcon(decisionTreeImage));

            // Refresh the frame to display the updated label
            revalidate();
            repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading decision tree image.",
                    "Image Loading Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DecisionTreeResultsGUI gui = new DecisionTreeResultsGUI();
            gui.setVisible(true);
        });
    }
}
