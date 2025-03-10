package projet_scrapping;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main {
    public static void main(String[] args) throws IOException, SQLException {
        String url = "https://www.emploi.ma/recherche-jobs-maroc?f%5B0%5D=im_field_offre_metiers%3A31";
      //  sql connect
        String jdbcUrl = "jdbc:mysql://localhost:3306/testjava";
        String user = "root";
        String password = "";
        Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

        try {
           //Connection connection = DriverManager.getConnection(jdbcUrl, user, password);

            Document document = Jsoup.connect(url).get();
            Elements h5Elements = document.select(".row h5");

            for (Element h5Element : h5Elements) {
                Element annonceLink = h5Element.select("a").first();
                String annonceUrl = annonceLink.attr("abs:href");
                //System.out.println(annonceUrl);
                
                Document urlpage = Jsoup.connect(annonceUrl).get();
                // System.out.println("Connected to: " + annonceUrl);
                
                //extraire titre
                Elements Titre = urlpage.select("h1.title");
                //System.out.println("Titre: " + Titre);
                String titreemploi = Titre.text();
                System.out.println("Titre: " + titreemploi);
                
                //extraire URL
                String urloffre = annonceUrl;
                System.out.println("url: " + urloffre);
                
                //Site Name
                String sitename = "www.emploi.ma";
                System.out.println("Site Name: " + sitename);
                
                //Date de publication
                Elements dp = urlpage.select("div.job-ad-publication-date");
               // System.out.println("Titre: " + dp);
                String datedepublcation = dp.text();
                System.out.println("Titre: " + datedepublcation);
                
                //Date pour postuler
                String datedepostule = "Null";
                System.out.println("date de postule: " + datedepostule );
                
                //Adresse entreprise
                String adressebtreprise = "Null";
                System.out.println("Adresse entreprise: " + adressebtreprise );
                
                //Site web entreprise
                Elements st = urlpage.select("td.website-url a");
                //System.out.println("site web entreprise: " + st);
                String siteentreprise = st.attr("href").trim();
                System.out.println("site de l'entreprise: " + siteentreprise);
                
                //Nom entreprise
                Elements et = urlpage.select("div.company-title");
                //System.out.println("Nom entreprise: " + et);
                String Nomentreprise = et.text();
                System.out.println("Nom entreprise: " + Nomentreprise);
                
                //Description entreprise
                Elements ut = urlpage.select("div.job-ad-company-description");
               // System.out.println("Description entreprise: " + ut);
                String Descriptionentreprise = ut.text().trim();
                System.out.println("Description entreprise: " + Descriptionentreprise);
               
                //descripion du poste 
                Elements dpp = urlpage.select(".ad-ss-title + p");
                // System.out.println("descripion du poste: " + dpp);
                String description = dpp.text().trim();
                  System.out.println("Description entreprise: " + description);
                 
                
                
                //Région
                Elements nn = urlpage.select("td:containsOwn(Région) + td div.field-item");
                //System.out.println("Regon: " + nn);
                String region = nn.text().trim();
                System.out.println("region: " + region);
                
                //ville
                Elements nnb = urlpage.select("td:contains(Ville) + td");
                //System.out.println("ville: " + nn);
                String ville = nnb.text();
                System.out.println("ville :"+ ville);
                
                //Secteur activité
                Elements sc = urlpage.select("td.sector-label:containsOwn(Secteur d´activité) + td div.field-item");
                //System.out.println("Regon: " + nn);
                String secteuractivite = sc.text().trim();
                System.out.println("secteur activite: " + secteuractivite);
                
                //Métier
                Elements p = urlpage.select("td.first-cell:contains(Métier) + td div.field-item");
                //System.out.println("Regon: " + nn);
                String metie = p.text().trim();
                System.out.println("Metie :"+metie);
                
                //Type du contrat
                Elements tpc = urlpage.select("td:contains(Type de contrat) + td div.field-item");
                //System.out.println("Regon: " + nn);
                String typedecontrat = tpc.text().trim();
                System.out.println("type de contrat :"+typedecontrat);
                
                
                //Niveau d'études
                Elements nic = urlpage.select("td:contains(Niveau d\\'études) + td div.field-item");
               // System.out.println("Niveau d'études: " + nic);
                String nivetude = nic.text().trim();
                System.out.println("Niveau d'études: " + nivetude);
                
                //Spécialité/ Diplôme???
                List<String> diplome = Arrays.asList("Bac+2","bac+3","bac+4","bac+5","master","licence","doctorat");
                String pagediplom = urlpage.text();
                List<String> foundDiplome = new ArrayList<>();

                for (String word : pagediplom.split("\\s+")) {
                    // You can adjust this based on case sensitivity and special characters
                    String cleanedWord = word.replaceAll("[^a-zA-Z]", "");

                    if (diplome.contains(cleanedWord)) {
                        foundDiplome.add(cleanedWord);
                    }
                }

                if (foundDiplome.isEmpty()) {
                    System.out.println("diplome: -");
                    foundDiplome.add("Null");
                } else {
                    System.out.println("Found diploma levels: " + foundDiplome);
                }
                /*boolean founddiplome = false;
                for (String word : pagediplom.split("\\s+")) {
                    String cleanedWord = word.replaceAll("[^a-zA-Z]", "");
                    
                    if (diplome.contains(cleanedWord)) {
                        System.out.print("diplome :"+cleanedWord + " ");
                        founddiplome = true;
                    }
                                      
                }
                if (!founddiplome) {
                    System.out.println("diplome :"+"-");
                }
                
                
                
                */
                //Expérience
                Elements exp = urlpage.select("td:contains(Niveau d\\'expérience ) + td div.field-item");
                // System.out.println("Niveau d'études: " + nic);
                 String Exeprience = exp.text().trim();
                 System.out.println("Expérience : " + Exeprience);
                
                             
                //Profil recherché
                Elements uv = urlpage.select(".ad-ss-title +  ul");
                 String Profilrecherche= uv.text();
                System.out.println("Profil recherché: " + Profilrecherche);
                
                //Traits de personnalité
                List<String> persona = Arrays.asList(
                		"autonomie",
                 		"capacité à être force de proposition",
                 		"humilité ",
                 	    "communication efficace",
                         "travail d'équipe",
                         "résolution de problèmes",
                         "adaptabilité",
                         "gestion du temps",
                         "esprit critique",
                         "leadership",
                         "gestion du stress",
                         "prise de décision",
                         "créativité",
                         "empathie",
                         "collaboration",
                         "pensée analytique",
                         "gestion des conflits",
                         "orientation client",
                         "autorégulation",
                         "initiative",
                         "persévérance",
                         "positivité",
                         "capacité à apprendre rapidement",
                         "confiance en soi",
                         "aventureux",
                         "aimable",
                         "analytique",
                         "altruiste",
                         "assertif",
                         "calme",
                         "capable",
                         "charismatique",
                         "chaleureux",
                         "charmant",
                         "collaboratif",
                         "communicatif",
                         "compétitif",
                         "compréhensif",
                         "consciencieux",
                         "creéatif",
                         "curieux",
                         "déterminé",
                         "direct",
                         "discipliné",
                         "dynamique",
                         "éclectique",
                         "équilibré",
                         "énergique",
                         "enthousiaste",
                         "épicurien",
                         "expressif",
                         "extraverti",
                         "facétieux",
                         "fiable",
                         "flexible",
                         "généreux",
                         "habile",
                         "honnéte",
                         "humble",
                         "imaginatif",
                         "intellectuel",
                         "jovial",
                         "modeste",
                         "optimiste",
                         "persévérant",
                         "pragmatique",
                         "précis",
                         "rigoureux",
                         "réaliste",  
                         "sage",  
                         "respectueux",  
                         "sensible",  
                         "serviable",  
                         "sincère",  
                         "talentueux",  
                         "volontaire",  
                         "audacieux"
                );
                
                String pageperso = urlpage.text();
                List<String> foundPersonaSkills = new ArrayList<>();

                for (String word : pageperso.split("\\s+")) {
                    // You can adjust this based on case sensitivity and special characters
                    String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                    if (persona.contains(cleanedWords)) {
                        foundPersonaSkills.add(cleanedWords);
                    }
                }

                if (foundPersonaSkills.isEmpty()) {
                    System.out.println("soft skills: -");
                    foundPersonaSkills.add("Null");
                } else {
                    System.out.println("Found persona skills: " + foundPersonaSkills);
                }
                /*boolean foundperso = false;
                for (String word : pageperso.split("\\s+")) {
                    // Vous pouvez ajuster cela en fonction de la casse et des caractères spéciaux
                    String cleanedWords = word.replaceAll("[^a-zA-Z]", "");
                    
                    if (persona.contains(cleanedWords)) {
                        System.out.print("soft skills :"+cleanedWords + " ");
                        foundperso = true;
                    }
                                      
                }
                if (!foundperso) {
                    System.out.println("soft skills :"+"-");
                }
                */
                
                //hard skills
                List<String> hardSkillsList = Arrays.asList("Java", "Python", "C++", "JavaScript", "C#", "Ruby", "Swift", "PHP","HTML/CSS", "React", "Angular", "Vue.js", "Node.js", "Express.js", "RESTful API","SQL" ,"MySQL", "PostgreSQL", "Oracle", "NoSQL" ,"MongoDB", "Cassandra", "Conception de bases de données","Linux","Unix", "Windows Server","TCP/IP", "Routage et commutation", "Configuration de routeurs et de commutateurs","Pare-feu" ,"firewalls", "Cryptographie", "Tests d'intrusion","VMware", "Docker", "Amazon Web Services" ,"AWS", "Microsoft Azure", "Google Cloud Platform" ,"GCP","Configuration et maintenance de serveurs", "Gestion des utilisateurs et des permissions","Jenkins", "GitLab CI", "Travis CI","Android", "Java","Kotlin", "iOS" ,"Swift","Data mining", "Machine learning", "TensorFlow, PyTorch","Scripting" ,"Shell", "PowerShell", "Ansible", "Chef, Puppet","XML, JSON", "GraphQL", "YAML","Agile", "Scrum", "Kanban","UML", "Unified Modeling Language", "Architecture logicielle","Profilage de code", "Optimisation de la performance");
                String pageText = urlpage.text();
                List<String> foundHardSkills = new ArrayList<>();
                for (String word : pageText.split("\\s+")) {
                    // You can adjust this based on case sensitivity and special characters
                    String cleanedWord = word.replaceAll("[^a-zA-Z]", "");

                    if (hardSkillsList.contains(cleanedWord)) {
                        foundHardSkills.add(cleanedWord);
                    }
                }

                if (foundHardSkills.isEmpty()) {
                    System.out.println("hard skills: -");
                    foundHardSkills.add("Null");
                } else {
                    System.out.println("Found hard skills: " + foundHardSkills);
                }

                /*
                boolean foundHardSkills = false;
                for (String word : pageText.split("\\s+")) {
                    // Vous pouvez ajuster cela en fonction de la casse et des caractères spéciaux
                    String cleanedWord = word.replaceAll("[^a-zA-Z]", "");
                    
                    if (hardSkillsList.contains(cleanedWord)) {
                        System.out.print("hard skills :"+cleanedWord + " ");
                        foundHardSkills = true;
                    }
                                      
                }
                if (!foundHardSkills) {
                    System.out.println("hard skills :"+"-");
                }*/
                //soft skills
                List<String> softSkillsList = Arrays.asList(
                		"autonomie",
                 		"capacité à être force de proposition",
                 		"humilité ",
                 	    "communication efficace",
                         "travail d'équipe",
                         "résolution de problèmes",
                         "adaptabilité",
                         "gestion du temps",
                         "esprit critique",
                         "leadership",
                         "gestion du stress",
                         "prise de décision",
                         "créativité",
                         "empathie",
                         "collaboration",
                         "pensée analytique",
                         "gestion des conflits",
                         "orientation client",
                         "autorégulation",
                         "initiative",
                         "persévérance",
                         "positivité",
                         "capacité à apprendre rapidement",
                         "résolution de problèmes",
                         "pensée critique",
                         "empathie",
                         "apprentissage continu",
                         "persuasion",
                         "organisation",
                         "rigueur",
                         "écoute active",
                         "ouverture d'esprit",
                         "capacité à travailler sous pression",
                         "gestion de conflits",
                         "orientation vers la qualité",
                         "ténacité",
                         "vision stratégique",
                         "sens de l'urgence",
                         "sens de l'humour",
                         "sens de l'organisation",
                         "souci du détail",
                         "orientation vers les détails",
                         "prise d'initiative",
                         "esprit d'équipe",
                         "oritentation vers le client",
                         "transformation",
                         "participation",
                         "motivation",
                         "inspiration",
                         "vision",
                         "vision stratégique",
                         "éthique",
                         "servant",
                         "autocratie",
                         "charisme",
                         "innovation",
                         "collaboration",
                         "diversité",
                         "durabilité",
                         "qualité",
                         "responsabilité",
                         "technologie",
                         "recherche",
                         "analyse",
                         "efficacité",
                         "flexibilité",
                         "prise de décision",
                         "client",
                         "agile",
                         "authentique",
                         "adaptation",
                         "influence",
                         "résilience",
                         "durabilité",
                         "communication interculturelle",
                         "axé sur les résultats",
                         "coach",
                         "croissance",
                         "équilibré"
                );
                String pagesText = urlpage.text();
                List<String> foundSoftSkills = new ArrayList<>();
                for (String word : pagesText.split("\\s+")) {
                    // You can adjust this based on case sensitivity and special characters
                    String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                    if (softSkillsList.contains(cleanedWords)) {
                        foundSoftSkills.add(cleanedWords);
                    }
                }

                if (foundSoftSkills.isEmpty()) {
                    System.out.println("soft skills : -");
                    foundSoftSkills.add("Null");
                } else {
                    System.out.println("Found soft skills: " + foundSoftSkills);
                }

                /*
                String pagesText = urlpage.text();
                boolean foundsoftSkills = false;
                for (String word : pagesText.split("\\s+")) {
                    // Vous pouvez ajuster cela en fonction de la casse et des caractères spéciaux
                    String cleanedWords = word.replaceAll("[^a-zA-Z]", "");
                    
                    if (softSkillsList.contains(cleanedWords)) {
                        System.out.print("soft skills :"+cleanedWords + " ");
                        foundsoftSkills = true;
                    }
                                      
                }
                if (!foundsoftSkills) {
                    System.out.println("soft skills :"+"-");
                }*/
                
                //23.	Compétences recommandées
                Elements cmr = urlpage.select("td:contains(Compétences clés) + td div.field-item");
                // System.out.println("Niveau d'études: " + nic);
                 String competence = cmr.text().trim();
                 System.out.println("Compétences recommandées : " + competence);
                 
                 //24.	Langue
                  Elements langee = urlpage.select("span.lineage-item-level-0");
                  //System.out.println(" langue: " + niveaulange);
                  String lange = langee.text().trim();
                  System.out.println("Langue: " + lange);
                  
                  //25.	Niveau de la langue
                  Elements niveauu = urlpage.select("span.lineage-item-level-1");
                  //System.out.println(" langue: " + niveaulange);
                  String niveau = niveauu.text().trim();
                  System.out.println("niveau Langue: " + niveau);
                  
                  
                  
                  //sailre
                  String salaire = "Null";
                  System.out.println("salaire: " + salaire);
                  
                  /*List<String> salaire = Arrays.asList(
                  		"salaire ","dhs"
                  );
                  String pagesalaire = urlpage.text();
                  boolean foundsalaire = false;
                  for (String word : pagesalaire.split("\\s+")) {
                      String cleanedWords = word.replaceAll("[^a-zA-Z]", "");
                      
                      if (salaire.contains(cleanedWords)) {
                         System.out.print("salaire :"+cleanedWords + " ");
                          foundsalaire = true;
                      }
                                        
                  }
                  if (!foundsalaire) {
                      System.out.println("salaire :"+"-");
                  }*/
                  
                  //Avantages sociaux
                  List<String> avantages = Arrays.asList(
                    		"Assurance santé ","Congés payés","Retraite d'entreprise","Programmes de bien-être",
                    		"Assurance dentaire et visuelle","Assurance vie et assurance décès",
                    		"Congés de maternité et de paternité","Congés familiaux",
                    		"Formation et développement professionnel","Remboursement des frais de scolarité",
                    		"Prime de performance","Horaires de travail flexibles",
                    		"Assurance invalidité","Assurance voyage","Rabais et avantages supplémentaires"
                    		
                    );
                  List<String> foundAvantages = new ArrayList<>();
                  String pageavantage = urlpage.text();
                  for (String word : pageavantage.split("\\s+")) {
                      String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                      if (avantages.contains(cleanedWords)) {
                          foundAvantages.add(cleanedWords);
                      }
                  }
                  if (foundAvantages.isEmpty()) {
                      System.out.println("avantages : -");
                      foundAvantages.add("Null");
                  } else {
                      System.out.println("Found avantages: " + foundAvantages);
                  }

                   /* String pageavantage = urlpage.text();
                    boolean foundavanatage = false;
                    for (String word : pageavantage.split("\\s+")) {
                        String cleanedWords = word.replaceAll("[^a-zA-Z]", "");
                        
                        if (avantages.contains(cleanedWords)) {
                           System.out.print("salaire :"+cleanedWords + " ");
                           foundavanatage = true;
                        }
                                          
                    }
                    if (!foundavanatage) {
                        System.out.println("avantages :"+"-");
                    }*/
                    //teletravail
                  List<String> telet = Arrays.asList("Télétravail,Travail En Remote");
                  String pageatele = urlpage.text();
                  boolean foundtele = false;

                  for (String word : pageatele.split("\\s+")) {
                      String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                      if (telet.contains(cleanedWords)) {
                          foundtele = true;
                          break;  // No need to continue checking once found
                      }
                  }

                  // Store the result as a String
                  String teletravail = foundtele ? "oui" : "non";
                  System.out.println("teletravail : " + teletravail);
                    
                    ///insert
                   String insertQuery = "INSERT INTO emploi (titre,url,sitename,datedepublication,sitewebdentreprise,descriptiondelentreprise,descriptiondeposte,region,ville,secteurdactivite,metier,typedecontrat,niveaudetude,diplome,experience,profilerecherche,traitsdepersonnalite,hardskills,softskills,competencerecommandees,langue,niveaudelalangue,salaire,avantagesociaux,teletravail,nomdentreprise) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                   try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        
                	   preparedStatement.setString(1, titreemploi);
                       preparedStatement.setString(2, urloffre);
                       preparedStatement.setString(3, sitename);
                       preparedStatement.setString(4, datedepublcation);
                       preparedStatement.setString(5, siteentreprise);
                       preparedStatement.setString(6, Descriptionentreprise);
                       preparedStatement.setString(7, description);
                       preparedStatement.setString(8, region);
                       preparedStatement.setString(9, ville);
                       preparedStatement.setString(10, secteuractivite);
                       preparedStatement.setString(11, metie);
                       preparedStatement.setString(12, typedecontrat);
                       preparedStatement.setString(13, nivetude);
                       
                       String diplomeAsString = String.join(",", foundDiplome);
                       preparedStatement.setString(14, diplomeAsString);
                       
                       preparedStatement.setString(15, Exeprience);
                       preparedStatement.setString(16, Profilrecherche);
                       
                       String PERSONSKILLSAsString = String.join(",", foundPersonaSkills);
                       preparedStatement.setString(17, PERSONSKILLSAsString);
                       
                       String foundHardSkillsString = String.join(",", foundHardSkills);
                       preparedStatement.setString(18, foundHardSkillsString);
                       
                       String foundSoftSkillsString = String.join(",", foundSoftSkills);
                       preparedStatement.setString(19, foundSoftSkillsString);
                       
                       preparedStatement.setString(20, competence);
                       preparedStatement.setString(21, lange);
                       preparedStatement.setString(22, niveau);
                       preparedStatement.setString(23, salaire);
                       
                       String foundAvantagesString = String.join(",", foundAvantages);
                       preparedStatement.setString(24, foundAvantagesString);
                       
                       String teletravailString = String.join(",", teletravail);
                       preparedStatement.setString(25, teletravail);
                       preparedStatement.setString(26, Nomentreprise);
                       
                       
                            

                            // Exécuter la requête d'insertion
                            preparedStatement.executeUpdate();
                        }
                  
                  
                  
                  

                
                
                
                System.out.println("*******************************************************\n");
                
                
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
       // System.out.println("*******************************************************\n"); les autre pages 
        for(int i=1;i<=3;i++) {
    		
            String urlpages = "https://www.emploi.ma/recherche-jobs-maroc?f%5B0%5D=im_field_offre_metiers%3A31&page=";
            
            try {
            	Document document = Jsoup.connect(urlpages+i).get();
                Elements h5Elements = document.select(".row h5");
               // System.out.println(h5Elements);
                for (Element h5Element : h5Elements) {
                	 Element annonceLink = h5Element.select("a").first();
                     String annonceUrl = annonceLink.attr("abs:href");
                     //System.out.println(annonceUrl);
                     
                     Document urlpage = Jsoup.connect(annonceUrl).get();
                     // System.out.println("Connected to: " + annonceUrl);
                     
                     //extraire titre
                     Elements Titre = urlpage.select("h1.title");
                     //System.out.println("Titre: " + Titre);
                     String titreemploi = Titre.text();
                     System.out.println("Titre: " + titreemploi);
                     
                     //extraire URL
                     String urloffre = annonceUrl;
                     System.out.println("url: " + urloffre);
                     
                     //Site Name
                     String sitename = "www.emploi.ma";
                     System.out.println("Site Name: " + sitename);
                     
                     //Date de publication
                     Elements dp = urlpage.select("div.job-ad-publication-date");
                    // System.out.println("Titre: " + dp);
                     String datedepublcation = dp.text();
                     System.out.println("Titre: " + datedepublcation);
                     
                     //Date pour postuler
                     String datedepostule = "Null";
                     System.out.println("date de postule: " + datedepostule );
                     
                     //Adresse entreprise
                     String adressebtreprise = "Null";
                     System.out.println("Adresse entreprise: " + adressebtreprise );
                     
                     //Site web entreprise
                     Elements st = urlpage.select("td.website-url a");
                     //System.out.println("site web entreprise: " + st);
                     String siteentreprise = st.attr("href").trim();
                     System.out.println("site de l'entreprise: " + siteentreprise);
                     
                     //Nom entreprise
                     Elements et = urlpage.select("div.company-title");
                     //System.out.println("Nom entreprise: " + et);
                     String Nomentreprise = et.text();
                     System.out.println("Nom entreprise: " + Nomentreprise);
                     
                     //Description entreprise
                     Elements ut = urlpage.select("div.job-ad-company-description");
                    // System.out.println("Description entreprise: " + ut);
                     String Descriptionentreprise = ut.text().trim();
                     System.out.println("Description entreprise: " + Descriptionentreprise);
                     
                     //descripion du poste 
                     Elements dpp = urlpage.select(".ad-ss-title + p");
                     // System.out.println("descripion du poste: " + dpp);
                     String description = dpp.text().trim();
                       System.out.println("Description entreprise: " + description);
                      
                     
                     
                     //Région
                     Elements nn = urlpage.select("td:containsOwn(Région) + td div.field-item");
                     //System.out.println("Regon: " + nn);
                     String region = nn.text().trim();
                     System.out.println("region: " + region);
                     
                     //ville
                     Elements nnb = urlpage.select("td:contains(Ville) + td");
                     //System.out.println("ville: " + nn);
                     String ville = nnb.text();
                     System.out.println("ville :"+ ville);
                     
                     //Secteur activité
                     Elements sc = urlpage.select("td.sector-label:containsOwn(Secteur d´activité) + td div.field-item");
                     //System.out.println("Regon: " + nn);
                     String secteuractivite = sc.text().trim();
                     System.out.println("secteur activite: " + secteuractivite);
                     
                     //Métier
                     Elements p = urlpage.select("td.first-cell:contains(Métier) + td div.field-item");
                     //System.out.println("Regon: " + nn);
                     String metie = p.text().trim();
                     System.out.println("Metie :"+metie);
                     
                     //Type du contrat
                     Elements tpc = urlpage.select("td:contains(Type de contrat) + td div.field-item");
                     //System.out.println("Regon: " + nn);
                     String typedecontrat = tpc.text().trim();
                     System.out.println("type de contrat :"+typedecontrat);
                     
                     
                     //Niveau d'études
                     Elements nic = urlpage.select("td:contains(Niveau d\\'études) + td div.field-item");
                    // System.out.println("Niveau d'études: " + nic);
                     String nivetude = nic.text().trim();
                     System.out.println("Niveau d'études: " + nivetude);
                     
                   //Spécialité/ Diplôme???
                     List<String> diplome = Arrays.asList("Bac+2","bac+3","bac+4","bac+5","master","licence","doctorat");
                     String pagediplom = urlpage.text();
                     List<String> foundDiplome = new ArrayList<>();

                     for (String word : pagediplom.split("\\s+")) {
                         // You can adjust this based on case sensitivity and special characters
                         String cleanedWord = word.replaceAll("[^a-zA-Z]", "");

                         if (diplome.contains(cleanedWord)) {
                             foundDiplome.add(cleanedWord);
                         }
                     }

                     if (foundDiplome.isEmpty()) {
                         System.out.println("diplome: -");
                         foundDiplome.add("Null");
                     } else {
                         System.out.println("Found diploma levels: " + foundDiplome);
                     }
                     
                     
                     
                     //Expérience
                     Elements exp = urlpage.select("td:contains(Niveau d\\'expérience ) + td div.field-item");
                     // System.out.println("Niveau d'études: " + nic);
                      String Exeprience = exp.text().trim();
                      System.out.println("Expérience : " + Exeprience);
                     
                                  
                     //Profil recherché
                     Elements uv = urlpage.select(".ad-ss-title +  ul");
                      String Profilrecherche= uv.text();
                     System.out.println("Profil recherché: " + Profilrecherche);
                     
                   //Traits de personnalité
                     List<String> persona = Arrays.asList(
                    		 "autonomie",
                      		"capacité à être force de proposition",
                      		"humilité ",
                      	    "communication efficace",
                              "travail d'équipe",
                              "résolution de problèmes",
                              "adaptabilité",
                              "gestion du temps",
                              "esprit critique",
                              "leadership",
                              "gestion du stress",
                              "prise de décision",
                              "créativité",
                              "empathie",
                              "collaboration",
                              "pensée analytique",
                              "gestion des conflits",
                              "orientation client",
                              "autorégulation",
                              "initiative",
                              "persévérance",
                              "positivité",
                              "capacité à apprendre rapidement",
                              "confiance en soi",
                              "aventureux",
                              "aimable",
                              "analytique",
                              "altruiste",
                              "assertif",
                              "calme",
                              "capable",
                              "charismatique",
                              "chaleureux",
                              "charmant",
                              "collaboratif",
                              "communicatif",
                              "compétitif",
                              "compréhensif",
                              "consciencieux",
                              "creéatif",
                              "curieux",
                              "déterminé",
                              "direct",
                              "discipliné",
                              "dynamique",
                              "éclectique",
                              "équilibré",
                              "énergique",
                              "enthousiaste",
                              "épicurien",
                              "expressif",
                              "extraverti",
                              "facétieux",
                              "fiable",
                              "flexible",
                              "généreux",
                              "habile",
                              "honnéte",
                              "humble",
                              "imaginatif",
                              "intellectuel",
                              "jovial",
                              "modeste",
                              "optimiste",
                              "persévérant",
                              "pragmatique",
                              "précis",
                              "rigoureux",
                              "réaliste",  
                              "sage",  
                              "respectueux",  
                              "sensible",  
                              "serviable",  
                              "sincère",  
                              "talentueux",  
                              "volontaire",  
                              "audacieux"
                     );
                     
                     String pageperso = urlpage.text();
                     List<String> foundPersonaSkills = new ArrayList<>();

                     for (String word : pageperso.split("\\s+")) {
                         // You can adjust this based on case sensitivity and special characters
                         String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                         if (persona.contains(cleanedWords)) {
                             foundPersonaSkills.add(cleanedWords);
                         }
                     }

                     if (foundPersonaSkills.isEmpty()) {
                         System.out.println("soft skills: -");
                         foundPersonaSkills.add("Null");
                     } else {
                         System.out.println("Found persona skills: " + foundPersonaSkills);
                     }
                     
                   //hard skills
                     List<String> hardSkillsList = Arrays.asList("Java", "Python", "C++", "JavaScript", "C#", "Ruby", "Swift", "PHP","HTML/CSS", "React", "Angular", "Vue.js", "Node.js", "Express.js", "RESTful API","SQL" ,"MySQL", "PostgreSQL", "Oracle", "NoSQL" ,"MongoDB", "Cassandra", "Conception de bases de données","Linux","Unix", "Windows Server","TCP/IP", "Routage et commutation", "Configuration de routeurs et de commutateurs","Pare-feu" ,"firewalls", "Cryptographie", "Tests d'intrusion","VMware", "Docker", "Amazon Web Services" ,"AWS", "Microsoft Azure", "Google Cloud Platform" ,"GCP","Configuration et maintenance de serveurs", "Gestion des utilisateurs et des permissions","Jenkins", "GitLab CI", "Travis CI","Android", "Java","Kotlin", "iOS" ,"Swift","Data mining", "Machine learning", "TensorFlow, PyTorch","Scripting" ,"Shell", "PowerShell", "Ansible", "Chef, Puppet","XML, JSON", "GraphQL", "YAML","Agile", "Scrum", "Kanban","UML", "Unified Modeling Language", "Architecture logicielle","Profilage de code", "Optimisation de la performance");
                     String pageText = urlpage.text();
                     List<String> foundHardSkills = new ArrayList<>();
                     for (String word : pageText.split("\\s+")) {
                         // You can adjust this based on case sensitivity and special characters
                         String cleanedWord = word.replaceAll("[^a-zA-Z]", "");

                         if (hardSkillsList.contains(cleanedWord)) {
                             foundHardSkills.add(cleanedWord);
                         }
                     }

                     if (foundHardSkills.isEmpty()) {
                         System.out.println("hard skills: -");
                         foundHardSkills.add("Null");
                     } else {
                         System.out.println("Found hard skills: " + foundHardSkills);
                     }
                   //soft skills
                     List<String> softSkillsList = Arrays.asList(
                    		 "autonomie",
                      		"capacité à être force de proposition",
                      		"humilité ",
                      	    "communication efficace",
                              "travail d'équipe",
                              "résolution de problèmes",
                              "adaptabilité",
                              "gestion du temps",
                              "esprit critique",
                              "leadership",
                              "gestion du stress",
                              "prise de décision",
                              "créativité",
                              "empathie",
                              "collaboration",
                              "pensée analytique",
                              "gestion des conflits",
                              "orientation client",
                              "autorégulation",
                              "initiative",
                              "persévérance",
                              "positivité",
                              "capacité à apprendre rapidement",
                              "résolution de problèmes",
                              "pensée critique",
                              "empathie",
                              "apprentissage continu",
                              "persuasion",
                              "organisation",
                              "rigueur",
                              "écoute active",
                              "ouverture d'esprit",
                              "capacité à travailler sous pression",
                              "gestion de conflits",
                              "orientation vers la qualité",
                              "ténacité",
                              "vision stratégique",
                              "sens de l'urgence",
                              "sens de l'humour",
                              "sens de l'organisation",
                              "souci du détail",
                              "orientation vers les détails",
                              "prise d'initiative",
                              "esprit d'équipe",
                              "oritentation vers le client",
                              "transformation",
                              "participation",
                              "motivation",
                              "inspiration",
                              "vision",
                              "vision stratégique",
                              "éthique",
                              "servant",
                              "autocratie",
                              "charisme",
                              "innovation",
                              "collaboration",
                              "diversité",
                              "durabilité",
                              "qualité",
                              "responsabilité",
                              "technologie",
                              "recherche",
                              "analyse",
                              "efficacité",
                              "flexibilité",
                              "prise de décision",
                              "client",
                              "agile",
                              "authentique",
                              "adaptation",
                              "influence",
                              "résilience",
                              "durabilité",
                              "communication interculturelle",
                              "axé sur les résultats",
                              "coach",
                              "croissance",
                              "équilibré"

                     );
                     String pagesText = urlpage.text();
                     List<String> foundSoftSkills = new ArrayList<>();
                     for (String word : pagesText.split("\\s+")) {
                         // You can adjust this based on case sensitivity and special characters
                         String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                         if (softSkillsList.contains(cleanedWords)) {
                             foundSoftSkills.add(cleanedWords);
                         }
                     }

                     if (foundSoftSkills.isEmpty()) {
                         System.out.println("soft skills : -");
                         foundSoftSkills.add("Null");
                     } else {
                         System.out.println("Found soft skills: " + foundSoftSkills);
                     }
                     
                     //23.	Compétences recommandées
                     Elements cmr = urlpage.select("td:contains(Compétences clés) + td div.field-item");
                     // System.out.println("Niveau d'études: " + nic);
                      String competence = cmr.text().trim();
                      System.out.println("Compétences recommandées : " + competence);
                      
                      //24.	Langue
                       Elements langee = urlpage.select("span.lineage-item-level-0");
                       //System.out.println(" langue: " + niveaulange);
                       String lange = langee.text().trim();
                       System.out.println("Langue: " + lange);
                       
                       //25.	Niveau de la langue
                       Elements niveauu = urlpage.select("span.lineage-item-level-1");
                       //System.out.println(" langue: " + niveaulange);
                       String niveau = niveauu.text().trim();
                       System.out.println("niveau Langue: " + niveau);
                       
                       
                       
                       //sailre
                       String salaire = "Null";
                       System.out.println("salaire: " + salaire);
                     //Avantages sociaux
                       List<String> avantages = Arrays.asList(
                         		"Assurance santé ","Congés payés","Retraite d'entreprise","Programmes de bien-être",
                         		"Assurance dentaire et visuelle","Assurance vie et assurance décès",
                         		"Congés de maternité et de paternité","Congés familiaux",
                         		"Formation et développement professionnel","Remboursement des frais de scolarité",
                         		"Prime de performance","Horaires de travail flexibles",
                         		"Assurance invalidité","Assurance voyage","Rabais et avantages supplémentaires"
                         		
                         );
                       List<String> foundAvantages = new ArrayList<>();
                       String pageavantage = urlpage.text();
                       for (String word : pageavantage.split("\\s+")) {
                           String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                           if (avantages.contains(cleanedWords)) {
                               foundAvantages.add(cleanedWords);
                           }
                       }
                       if (foundAvantages.isEmpty()) {
                           System.out.println("avantages : -");
                           foundAvantages.add("Null");
                       } else {
                           System.out.println("Found avantages: " + foundAvantages);
                       }

                         
                     //Télétravail
                       List<String> telet = Arrays.asList("Télétravail,Travail En Remote");
                       String pageatele = urlpage.text();
                       boolean foundtele = false;

                       for (String word : pageatele.split("\\s+")) {
                           String cleanedWords = word.replaceAll("[^a-zA-Z]", "");

                           if (telet.contains(cleanedWords)) {
                               foundtele = true;
                               break;  // No need to continue checking once found
                           }
                       } 

                       // Store the result as a String
                       String teletravail = foundtele ? "oui" : "non";
                       System.out.println("teletravail : " + teletravail);
                       
                         ///insert
                         String insertQuery = "INSERT INTO emploi (titre,url,sitename,datedepublication,sitewebdentreprise,descriptiondelentreprise,descriptiondeposte,region,ville,secteurdactivite,metier,typedecontrat,niveaudetude,diplome,experience,profilerecherche,traitsdepersonnalite,hardskills,softskills,competencerecommandees,langue,niveaudelalangue,salaire,avantagesociaux,teletravail,nomdentreprise) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                         try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                              
                         preparedStatement.setString(1, titreemploi);
                         preparedStatement.setString(2, urloffre);
                         preparedStatement.setString(3, sitename);
                         preparedStatement.setString(4, datedepublcation);
                         preparedStatement.setString(5, siteentreprise);
                         preparedStatement.setString(6, Descriptionentreprise);
                         preparedStatement.setString(7, description);
                         preparedStatement.setString(8, region);
                         preparedStatement.setString(9, ville);
                         preparedStatement.setString(10, secteuractivite);
                         preparedStatement.setString(11, metie);
                         preparedStatement.setString(12, typedecontrat);
                         preparedStatement.setString(13, nivetude);
                         
                         String diplomeAsString = String.join(",", foundDiplome);
                         preparedStatement.setString(14, diplomeAsString);
                         
                         preparedStatement.setString(15, Exeprience);
                         preparedStatement.setString(16, Profilrecherche);
                         
                         String PERSONSKILLSAsString = String.join(",", foundPersonaSkills);
                         preparedStatement.setString(17, PERSONSKILLSAsString);
                         
                         String foundHardSkillsString = String.join(",", foundHardSkills);
                         preparedStatement.setString(18, foundHardSkillsString);
                         
                         String foundSoftSkillsString = String.join(",", foundSoftSkills);
                         preparedStatement.setString(19, foundSoftSkillsString);
                         
                         preparedStatement.setString(20, competence);
                         preparedStatement.setString(21, lange);
                         preparedStatement.setString(22, niveau);
                         preparedStatement.setString(23, salaire);
                         
                         String foundAvantagesString = String.join(",", foundAvantages);
                         preparedStatement.setString(24, foundAvantagesString);
                         
                         String teletravailString = String.join(",", teletravail);
                         preparedStatement.setString(25, teletravail);
                         preparedStatement.setString(26, Nomentreprise);
                         
                         
                              

                              // Exécuter la requête d'insertion
                              preparedStatement.executeUpdate();
                          }
                       
                       

                     
                     
                     
                     System.out.println("*******************************************************\n");
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        
        
    }
    } 
    }