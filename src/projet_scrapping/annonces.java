package projet_scrapping;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class annonces {

    public static void main(String[] args) {
        String urlpages = "https://www.marocannonces.com/maroc/offres-emploi-domaine-informatique-multimedia-internet-b309.html?f_3=Informatique+%2F+Multim%C3%A9dia+%2F+Internet&pge=";
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String utilisateur = "root";
        String motDePasse = "";

        try {
            Connection connexion = DriverManager.getConnection(jdbcUrl, utilisateur, motDePasse);

            for (int i = 1; i <= 6; i++) {
                Document doc = Jsoup.connect(urlpages + i).get();
                Element carsList = doc.selectFirst("ul.cars-list");

                Elements listItems = carsList.select("li");
                for (Element listItem : listItems) {
                    Element link = listItem.selectFirst("a[href]");
                    if (link != null) {
                        String href = link.attr("href");

                        Document document = Jsoup.connect("https://www.marocannonces.com/" + href).get();
                        
                        Element baseTag = document.selectFirst("head > base");
                        String siteName = baseTag.attr("href");
                        String urlOffre = "https://www.marocannonces.com/" + href;
                        
                        Element h1Element = document.selectFirst(".description.desccatemploi h1");
                        String titre = h1Element != null ? h1Element.text() : "";

                        Element ulElement = document.select("ul.info-holder").first();
                        String ville = "", datepub = "";
                        if (ulElement != null) {
                            Elements liElements = ulElement.select("li");
                            if (!liElements.isEmpty()) {
                                Element firstLi = liElements.first();
                                Element firstLiLink = firstLi.selectFirst("a");
                                ville = (firstLiLink != null) ? firstLiLink.ownText().trim() : "";

                                Element secondLi = liElements.get(1);
                                if (secondLi != null) {
                                    datepub = secondLi.ownText().trim();
                                    datepub = datepub.replaceFirst("^Publiée le:\\s*", "");
                                    datepub = datepub.split("-")[0].trim();
                                }
                            }
                        }
                        
                        Element ulElement1 = document.selectFirst("ul.extraQuestionName");
                        String entreprise = "", secteur = "", contrat = "", salaire = "", metier = "", niveau_etudes = "";
                        if (ulElement1 != null) {
                            Elements liElements = ulElement1.select("li");
                            for (Element li : liElements) {
                                Element aTag = li.selectFirst("a");
                                if (aTag != null) {
                                    String textAfterLink = aTag.ownText().trim();
                                    if (li.text().startsWith("Entreprise")) {
                                        entreprise = textAfterLink;
                                    } else if (li.text().startsWith("Domaine")) {
                                        secteur = textAfterLink;
                                    } else if (li.text().startsWith("Contrat")) {
                                        contrat = textAfterLink;
                                    } else if (li.text().startsWith("Salaire")) {
                                        salaire = textAfterLink;
                                    } else if (li.text().startsWith("Fonction")) {
                                        metier = textAfterLink;
                                    } else if (li.text().startsWith("Niveau d'études")) {
                                        niveau_etudes = textAfterLink;
                                    }
                                }
                            }
                        }
                        
                        Element descriptionElement = document.selectFirst("div.description.desccatemploi > div.block");
                        String description = "";
                        if (descriptionElement != null) {
                            description = descriptionElement.text();
                        }
                        
                        System.out.println("*****************************");
                        System.out.println("- Site Name : " + siteName);
                        System.out.println("- URL : " + urlOffre);
                        System.out.println("- Titre : " + titre);
                        System.out.println("- Ville : " + ville);
                        System.out.println("- Date de publication : " + datepub);
                        System.out.println("- Entreprise : " + entreprise);
                        System.out.println("- Secteur : " + secteur);
                        System.out.println("- Contrat : " + contrat);
                        System.out.println("- Salaire : " + salaire);
                        System.out.println("- Métier : " + metier);
                        System.out.println("- Niveau d'études : " + niveau_etudes);
                        System.out.println("- Description : " + description);
                        System.out.println();

                        String sql = "INSERT INTO annonces (site_name, url, titre, ville, date_publication, entreprise, secteur, contrat, salaire, metier, niveau_etudes, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connexion.prepareStatement(sql);
                        statement.setString(1, siteName);
                        statement.setString(2, urlOffre);
                        statement.setString(3, titre);
                        statement.setString(4, ville);
                        statement.setString(5, datepub);
                        statement.setString(6, entreprise);
                        statement.setString(7, secteur);
                        statement.setString(8, contrat);
                        statement.setString(9, salaire);
                        statement.setString(10, metier);
                        statement.setString(11, niveau_etudes);
                        statement.setString(12, description);

                        statement.executeUpdate();
                        statement.close();
                    }
                }
            }
            connexion.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}






