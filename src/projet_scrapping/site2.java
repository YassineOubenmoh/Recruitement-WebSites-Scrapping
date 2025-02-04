package projet_scrapping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class site2 {
    public static void main(String[] args) throws IOException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            for (int i = 1; i <= 5; i++) {
                String urlpages = "https://www.rekrute.com/offres.html?p=" + i
                        + "&s=1&o=1&positionId%5B0%5D=13&positionId%5B1%5D=19&positionId%5B2%5D=23";
                Document doc = Jsoup.connect(urlpages).get();

                Element postData = doc.getElementById("post-data");
                Elements allPost = postData.getElementsByClass("post-id");
                for (Element eachPost : allPost) {
                    try {
                        processJobPost(eachPost, connection);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void processJobPost(Element eachPost, Connection connection) throws IOException, SQLException {
        Element firstdiv = eachPost.getElementsByClass("col-sm-2 col-xs-12").get(0);
        Element hrefdiv = firstdiv.getElementsByTag("div").get(1);
        Element aTag = hrefdiv.getElementsByTag("a").get(0);
        String hrefExtrait = aTag.attr("href");
        String href = "https://www.rekrute.com" + hrefExtrait;
        Document document = Jsoup.connect(href).get();

        System.out.println("*****************************");

        Elements metaTag = document.select("meta[property=og:site_name]");
        String siteName = metaTag.attr("content");
        System.out.println("- Site Name : " + siteName + "\n");

        String title = document.title();
        System.out.println("- Titre : " + title);

        String urlOffre = href;
        System.out.println("- URL : " + urlOffre);

        Elements dp = document.select("div[class=col-md-12 col-sm-12 col-xs-12] span.newjob");
        String datePostuler = dp.select("b").text();
        System.out.println("- Date pour postuler : " + datePostuler);

        Element dateElement = document.select("div[class=col-md-12 col-sm-12 col-xs-12] span.newjob").first();
        String datePub = dateElement.ownText().replace("- Postulez avant le", "").trim();
        System.out.println("- Publiée : " + datePub);

        Element ulElement = document.select("ul.featureInfo").first();

        Element firstLi = ulElement.select("li").get(0);
        String xp = firstLi.ownText().trim();
        System.out.println("- Expérience : " + xp);

        Element secondli = ulElement.select("li").get(1);
        String region = secondli.ownText().trim().replace("poste(s) sur ", "");
        System.out.println("- Région : " + region);

        Element thirdLi = ulElement.select("li").get(2);
        String nivEtude = thirdLi.ownText().trim();
        System.out.println("- Niveau d'étude : " + nivEtude);

        Element contratElement = document.select("span[title='Type de contrat']").first();
        String typeContrat = contratElement != null ? contratElement.text() : null;
        System.out.println("- Type de contrat : " + typeContrat);

        Element teletravailElement = document.select("span[title='Télétravail']").first();
        String teletravail = teletravailElement != null ? teletravailElement.text().replace("Télétravail : ", "") : null;
        System.out.println("- Télétravail : " + teletravail);

        //Element salaireElement = document.select("span[title='Salaire proposé']").first();
        //String salairePropose = salaireElement != null ? salaireElement.text().replace("Salaire proposé : ", "") : null;
        //System.out.println("- Salaire proposé : " + salairePropose);

        Element divTrait = document.select("div.col-md-12.blc").first();
        List<String> tagSkillsList = new ArrayList<>();
        Elements tagSkillsElements = divTrait.select("span.tagSkills");
        for (Element tagSkillsElement : tagSkillsElements) {
            String tagSkills = tagSkillsElement.text().trim();
            tagSkillsList.add(tagSkills);
        }
        String tagSkillsString = String.join(",", tagSkillsList);
        System.out.println("- Traits de personnalité souhaités : " + tagSkillsString);

        Element adressElement = document.select("span[id='address']").first();
        String adress = adressElement != null ? adressElement.ownText() : null;
        System.out.println("- Adresse de l'entreprise : " + adress);

        Element h2pElement = document.select("div.col-md-12.blc h2:contains(Profil recherché)").first();
        Element nextParagraph = h2pElement.nextElementSibling();
        String profilrech = nextParagraph.text().trim();
        System.out.println("- Profil recherché : " + profilrech);

        Element posteHeader = document.select("div.col-md-12.blc h2:contains(Poste :)").first();
        String poste = "";
        if (posteHeader != null) {
            Element sibling = posteHeader.nextElementSibling();
            StringBuilder texteApresPoste = new StringBuilder();
            while (sibling != null) {
                texteApresPoste.append(sibling.outerHtml());
                sibling = sibling.nextElementSibling();
            }
            poste = Jsoup.parse(texteApresPoste.toString()).text().trim();
            System.out.println("- Description du poste : " + poste);
        } else {
            System.out.println("- Description du poste : ");
        }

        insertDataIntoDatabase(connection, siteName, title, urlOffre, datePostuler, datePub, xp, region, nivEtude,
                typeContrat, teletravail, tagSkillsList, adress, profilrech, poste);
    }

    private static void insertDataIntoDatabase(Connection connection, String siteName, String title, String urlOffre,
            String datePostuler, String datePub, String xp, String region, String nivEtude, String typeContrat,
            String teletravail, List<String> tagSkillsList, String adress, String profilrech,
            String poste) throws SQLException {
        String sqlQuery = "INSERT INTO rekrute (site_name, title, url, date_postuler, date_pub, experience, region, niveau_etude, type_contrat, teletravail, traits_personnalite, adresse_entreprise, profil_recherche, description_poste) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, siteName);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, urlOffre);
            preparedStatement.setString(4, datePostuler);
            preparedStatement.setString(5, datePub);
            preparedStatement.setString(6, xp);
            preparedStatement.setString(7, region);
            preparedStatement.setString(8, nivEtude);
            preparedStatement.setString(9, typeContrat);
            preparedStatement.setString(10, teletravail);
            //preparedStatement.setString(11, salairePropose);
            preparedStatement.setString(11, String.join(",", tagSkillsList));
            preparedStatement.setString(12, adress);
            preparedStatement.setString(13, profilrech);
            preparedStatement.setString(14, poste);

            preparedStatement.executeUpdate();
        }
    }
}