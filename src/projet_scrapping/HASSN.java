package projet_scrapping;

import weka.classifiers.trees.REPTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HASSN {

    public static void main(String[] args) {
        try {
            // Chargement des données à partir du fichier ARFF
            DataSource source = new DataSource("C:\\Users\\apple\\Desktop\\tree\\cleanemploi.arff");
            Instances ensembleDonnees = source.getDataSet();

            // Spécification de l'indice de la colonne "ville"
            int indiceColonneCible = ensembleDonnees.attribute("ville").index();

            // Définition de la colonne cible comme classe
            ensembleDonnees.setClassIndex(indiceColonneCible);

            // Création du modèle d'arbre de décision avec l'algorithme REPTree
            REPTree modeleArbreDecision = new REPTree();
            modeleArbreDecision.buildClassifier(ensembleDonnees);

            // Affichage de l'arbre de décision
            afficherArbre(modeleArbreDecision, ensembleDonnees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Afficher l'arbre de décision dans une fenêtre
    private static void afficherArbre(REPTree modeleArbreDecision, Instances ensembleDonnees) throws Exception {
        final TreeVisualizer visualiseurArbre = new TreeVisualizer(null, modeleArbreDecision.graph(), new PlaceNode2());
        JFrame fenetreArbre = new JFrame("Visualiseur d'arbre de décision REPTree");
        fenetreArbre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetreArbre.setSize(800, 600);
        fenetreArbre.getContentPane().setLayout(new BorderLayout());
        fenetreArbre.getContentPane().add(visualiseurArbre, BorderLayout.CENTER);
        fenetreArbre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                fenetreArbre.dispose();
            }
        });

        fenetreArbre.setVisible(true);
        visualiseurArbre.fitToScreen();

        // Attendre jusqu'à ce que la fenêtre soit fermée
        fenetreArbre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                fenetreArbre.dispose();
            }
        });
    }
}