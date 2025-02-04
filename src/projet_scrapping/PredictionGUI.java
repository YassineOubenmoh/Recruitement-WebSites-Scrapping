package projet_scrapping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PredictionGUI {
    private JFrame frame;
    private JTextField[] inputFields;
    private JButton predictButton;
    private JLabel resultLabel;

    public PredictionGUI() {
        frame = new JFrame("Education Level Prediction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create input fields
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        String[] featureNames = {"Region", "Ville", "Secteur d'Activite", "Metier", "Type de Contrat",
                "Experience", "Langue"};
        inputFields = new JTextField[featureNames.length];

        for (int i = 0; i < featureNames.length; i++) {
            inputPanel.add(new JLabel(featureNames[i]));
            inputFields[i] = new JTextField();
            inputPanel.add(inputFields[i]);
        }

        // Create predict button
        predictButton = new JButton("Predict");
        predictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double prediction = calculatePrediction();
                resultLabel.setText("Prediction: " + prediction);
            }
        });

        // Create result label
        resultLabel = new JLabel("Prediction: ");

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(predictButton, BorderLayout.SOUTH);
        frame.add(resultLabel, BorderLayout.NORTH);

        // Set frame properties
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private double calculatePrediction() {
        // Parse input values and use the model's equation to calculate prediction
        double region = Double.parseDouble(inputFields[0].getText());
        double ville = Double.parseDouble(inputFields[1].getText());
        double secteur = Double.parseDouble(inputFields[2].getText());
        double metier = Double.parseDouble(inputFields[3].getText());
        double contrat = Double.parseDouble(inputFields[4].getText());
        double experience = Double.parseDouble(inputFields[5].getText());
        double langue = Double.parseDouble(inputFields[6].getText());

        double prediction = -0.0405 * region + 0.0223 * ville - 0.2595 * secteur
                + 0.2883 * metier - 0.1465 * contrat + 0.6741 * experience + 0.0863 * langue + 2.8619;

        return prediction;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PredictionGUI();
            }
        });
    }
}
