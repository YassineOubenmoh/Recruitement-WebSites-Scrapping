package projet_scrapping;



import weka.classifiers.functions.SimpleLinearRegression;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class hassan2{

    public static void main(String[] args) {
        try {
            // Chargement des données à partir du fichier ARFF
            DataSource source = new DataSource("C:\\Users\\apple\\Desktop\\tree\\cleanemploi.arff");
            Instances ensembleDonnees = source.getDataSet();

            // Spécification de l'indice de la colonne "typedecontrat"
            int indiceColonneCible = ensembleDonnees.attribute("typedecontrat").index();

            // Définition de la colonne cible comme attribut de classe
            ensembleDonnees.setClassIndex(indiceColonneCible);

            // Création du modèle de régression linéaire simple
            SimpleLinearRegression modeleRegression = new SimpleLinearRegression();
            modeleRegression.buildClassifier(ensembleDonnees);

            // Création d'une série de données pour la courbe de régression
            XYSeries serie = new XYSeries("Régression linéaire");
            for (int i = 0; i < ensembleDonnees.numInstances(); i++) {
                double x = ensembleDonnees.instance(i).value(0); // Assurez-vous que l'index correspond à la colonne "typedecontrat"
                double y = modeleRegression.classifyInstance(ensembleDonnees.instance(i));
                serie.add(x, y);
            }

            // Création de la collection de séries de données
            XYSeriesCollection dataset = new XYSeriesCollection(serie);

            // Création du graphique
            JFreeChart graphique = ChartFactory.createXYLineChart(
                    "Régression linéaire",
                    "Typedecontrat",
                    "Prédiction",
                    dataset
            );

            // Affichage du graphique sur un JFrame
            SwingUtilities.invokeLater(() -> {
                JFrame fenetre = new JFrame("Régression Linéaire");
                fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                fenetre.getContentPane().add(new ChartPanel(graphique), BorderLayout.CENTER);
                fenetre.setSize(800, 600);
                fenetre.setLocationRelativeTo(null);
                fenetre.setVisible(true);
            });

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}