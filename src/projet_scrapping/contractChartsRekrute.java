package projet_scrapping;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.CategoryLabelPositions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class contractChartsRekrute {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testjava";
        String utilisateur = "root";
        String motDePasse = "";

        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT type_contrat, COUNT(*) AS count FROM rekrute GROUP BY type_contrat");

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            System.out.println("Répartition des offres par type de type_contrat : ");

            while (resultSet.next()) {
                String type_contrat = resultSet.getString("type_contrat");

                // Vérifier si la clé n'est pas nulle avant de l'ajouter au dataset
                if (type_contrat != null) {
                    int count = resultSet.getInt("count");
                    dataset.setValue(count, "Offres", type_contrat);
                    System.out.println(type_contrat + " : " + count + " offres");
                }
            }

            if (dataset.getRowCount() > 0) {
                JFreeChart chart = ChartFactory.createBarChart(
                        "Répartition des offres par type de type_contrat pour le site Rekrute", // Titre du graphique
                        "Type de type_contrat", // Axe X (Catégorie)
                        "Nombre d'offres", // Axe Y
                        dataset);

                // Personnaliser les étiquettes des catégories (types de type_contrat)
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis axis = plot.getDomainAxis();
                axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotation des étiquettes

                ChartFrame frame = new ChartFrame("Graphique de type de type_contrat des offres", chart);
                frame.pack();
                frame.setVisible(true);
            } else {
                System.out.println("Aucun type de type_contrat trouvé.");
            }

            // Fermez les connexions
            resultSet.close();
            statement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}