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

public class nvEtudeChartsAnnonces {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testjava";
        String utilisateur = "root";
        String motDePasse = "";

        try {
            Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse);
            Statement statement = connexion.createStatement();
            
            // Modify the query to get count for each 'niveau_etudes' with occurrences more than 5
            String query = "SELECT niveau_etudes, COUNT(*) AS count FROM annonces GROUP BY niveau_etudes HAVING count > 5";
            
            ResultSet resultSet = statement.executeQuery(query);

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            System.out.println("Répartition des offres par niveau d'étude pour le site Rekrute (plus de 5 occurrences) : ");

            while (resultSet.next()) {
                String niveau_etudes = resultSet.getString("niveau_etudes");

                // Vérifier si la clé n'est pas nulle avant de l'ajouter au dataset
                if (niveau_etudes != null) {
                    int count = resultSet.getInt("count");
                    dataset.setValue(count, "Offres", niveau_etudes);
                    System.out.println(niveau_etudes + " : " + count + " offres");
                }
            }

            if (dataset.getRowCount() > 0) {
                JFreeChart chart = ChartFactory.createBarChart(
                        "Répartition des offres par niveau d'étude pour le site Maroc Annonces", // Titre du graphique
                        "Niveau d'étude", // Axe X (Catégorie)
                        "Nombre d'offres", // Axe Y
                        dataset);

                // Personnaliser les étiquettes des catégories (niveaux d'étude)
                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis axis = plot.getDomainAxis();
                axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // Rotation des étiquettes

                ChartFrame frame = new ChartFrame("Graphique de niveau d'étude des offres (plus de 5 occurrences)", chart);
                frame.pack();
                frame.setVisible(true);
            } else {
                System.out.println("Aucun niveau d'étude trouvé avec plus de 5 occurrences.");
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