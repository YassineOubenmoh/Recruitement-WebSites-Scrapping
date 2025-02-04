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
public class sectorsCharts {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testjava";
        String utilisateur = "root";
        String motDePasse = "";

        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT secteurdactivite, COUNT(*) AS count FROM emploi GROUP BY secteurdactivite HAVING count > 10");

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            System.out.println("Répartition des entreprises par secteur d'activité (plus de 10 occurrences) : ");

            while (resultSet.next()) {
                String secteurdactivite = resultSet.getString("secteurdactivite");

                // Vérifier si la clé n'est pas nulle avant de l'ajouter au dataset
                if (secteurdactivite != null) {
                    int count = resultSet.getInt("count");
                    dataset.setValue(count, "Entreprises", secteurdactivite);
                    System.out.println(secteurdactivite + " : " + count + " entreprises");
                }
            }

            if (dataset.getRowCount() > 0) {
                JFreeChart chart = ChartFactory.createBarChart(
                        "Répartition des entreprises par secteur d'activité (plus de 10 occurrences)", // Titre du graphique
                        "Secteur d'activité", // Axe X (Catégorie)
                        "Nombre d'entreprises", // Axe Y
                        dataset);

                // Personnaliser les étiquettes des catégories (secteurs d'activité)
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis axis = plot.getDomainAxis();
                axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotation des étiquettes

                ChartFrame frame = new ChartFrame("Graphique de secteur d'activité des entreprises (plus de 10 occurrences)", chart);
                frame.pack();
                frame.setVisible(true);
            } else {
                System.out.println("Aucun secteur d'activité avec plus de 10 occurrences.");
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
