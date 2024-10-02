package de.hda.fbi.db2.stud;

import de.hda.fbi.db2.tools.CsvDataReader;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 * to be anounced.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gsid;
    Map<String, Category> categories = new HashMap<>();
    private List<Category> catessssss;

    public Game() {
    }

    EntityManagerFactory emf;
    EntityManager em;



    public void printStart(){


        System.out.println("1: Stammdaten aus CSV-Datei laden\n" +
                "2: Wissenstest spielen\n" + "3: Spiele Simulieren\n" +
                "4: Analyse der Spieldaten\n" + "5: beenden\n\n");


        Scanner scan = new Scanner(System.in, "utf-8");
        int choice = scan.nextInt();
        switch(choice) {
            case 1:
                readCsv();
                printStart();
                break;
            case 2:
                getCategorysFromDB();
                play();
                break;
            case 3:
                simulate();
                break;
            case 4:
                Analysis analysis = new Analysis();
                analysis.chooseAnalyse();
                break;
            case 5:
                break;
            default:
                System.out.println("Sie haben keine gültige Eingabe gemacht");
        }

    }

    public void readCsv(){
        try {
            //Read default csv
            final List<String[]> defaultCsvLines = CsvDataReader.read();

            processCsv(defaultCsvLines);

            //Read (if available) additional csv-files and default csv-file
            List<String> availableFiles = CsvDataReader.getAvailableFiles();
            for (String availableFile: availableFiles){
                final List<String[]> additionalCsvLines = CsvDataReader.read(availableFile);
            }
        } catch (URISyntaxException use) {
            System.out.println(use);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        System.out.println("Die Datei wurde erfolgreich eingelesen");
    }



    private Map<String, Category> processCsv(List<String[]> lines){


        emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();

        boolean isPersisted = false;


        int linescounter = 0;
        int counter = 1;


        //erste Zeile nicht beachten
        lines.remove(0);

        System.out.println("Alle Fragen:");
        for (String[]line : lines){
            String qIDcsv = line[0];
            String descriptionCsv = line[1];
            String answer1Csv = line[2];
            String answer2Csv = line[3];
            String answer3Csv = line[4];
            String answer4Csv = line[5];
            String rightAnswerCsv = line[6];
            String categoryCsv = line[7];

            Answer answer1 = new Answer();
            Answer answer2 = new Answer();
            Answer answer3 = new Answer();
            Answer answer4 = new Answer();



            Question question = new Question();
            Category category = new Category();

            category.setName(categoryCsv);
            category.addQuestion(question);


            answer1.setDescription(answer1Csv);
            answer2.setDescription(answer2Csv);
            answer3.setDescription(answer3Csv);
            answer4.setDescription(answer4Csv);

            answer1.setQuestion(question);
            answer2.setQuestion(question);
            answer3.setQuestion(question);
            answer4.setQuestion(question);

            question.setID(Integer.parseInt(qIDcsv));
            question.setDescription(descriptionCsv);
            question.addAnswer(answer1);
            question.addAnswer(answer2);
            question.addAnswer(answer3);
            question.addAnswer(answer4);
            question.setCategory(category);

            int rightAnswer = (Integer.parseInt(rightAnswerCsv));
            question.getAnswers().get(rightAnswer - 1).setCorrectness(true);

            em.getTransaction().begin();
            linescounter++;
            Category exCat = persistCategory(em, category);
            question.setCategory(exCat);

            em.persist(exCat);
            if (categories.containsKey(category.getName())){
                categories.get(category.getName()).addQuestion(question);
            } else {
                categories.put(category.getName(), category);
            }

            em.persist(question);
            em.persist(answer1);
            em.persist(answer2);
            em.persist(answer3);
            em.persist(answer4);
            em.getTransaction().commit();
        }


        em.close();

        return categories;
    }

    public static Category persistCategory(EntityManager em, Category category){
        String query = ("select c from Category c where c.name =:name");

        List<Category> list = em.createQuery(query)
                .setParameter("name", category.getName()).getResultList();

        if (list.size() == 1){

            int categoryId = (list.get(0).getID());
            Category cat = em.find(Category.class, categoryId);
            return cat;
        }

        return category;
    }

    public void play(){

        emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();

        Scanner scan = new Scanner(System.in, "utf-8");
        System.out.println("Spielername eingeben:\n");
        String playername = scan.nextLine();

        Player player = new Player(playername);
        //Player player = getPlayersFromDB(playername);

        Gamesession gs = new Gamesession();
        em.getTransaction().begin();
        gs.setPlayer(player);

        Date start = new Date();
        gs.setStart(start);

        /*int numberOfCategories;
        System.out.println("Wie viele Kategorien möchten sie Spielen?");
        numberOfCategories = scan.nextInt();
        for(int i = 0; i < numberOfCategories;i++){
        }*/

        int numberOfCategories;
        System.out.println("Wie viele Kategorien möchtest du spielen?");
        numberOfCategories = scan.nextInt();


        int correct = 0;
        printCategories();
        List<Category> selectedCategories = new ArrayList<>();

        System.out.println("Tippe nun die Nummer deiner gewünschten Kategorie ein" +
                " und bestätige jeweils mit Enter!");
        for (int i = 0; i < numberOfCategories; i++){
            int catIndex = scan.nextInt();


            selectedCategories.add(catessssss.get(catIndex - 1));
            gs.addSelectedCategory(catessssss.get(catIndex - 1));

        }

        int numberOfQuestions;

        System.out.println("Wie viele Fragen willst du pro Kategorie gestellt bekommen?");
        numberOfQuestions = scan.nextInt();

        for (int i = 0; i < numberOfCategories; i++){
            while (numberOfQuestions > gs.getSelectedCategories().get(i).getQuestions().size()){
                numberOfQuestions--;
            }
        }




        gs.setNumberOfQuestion(numberOfQuestions);

        List<Integer> answers = new ArrayList<>();

        for (int j = 0; j < numberOfCategories; j++){


            for (int i = 0; i < numberOfQuestions; i++){

                Random rand = new Random();

                int randomQuestionIndex = rand.nextInt(gs.getSelectedCategories()
                        .get(j).getQuestions().size());

                Question randQuestion = gs.getSelectedCategories().get(j).getQuestions()
                        .get(randomQuestionIndex);

                System.out.println("Kategorie: " + gs.getSelectedCategories().get(j).getName());

                gs.addQuestion(randQuestion);

                System.out.println("Frage " +  (i + 1) + ": " + randQuestion.getDescription());


                System.out.println("1: " + randQuestion.getAnswers().get(0).getDescription());
                System.out.println("2: " + randQuestion.getAnswers().get(1).getDescription());
                System.out.println("3: " + randQuestion.getAnswers().get(2).getDescription());
                System.out.println("4: " + randQuestion.getAnswers().get(3).getDescription());

                int choice;
                System.out.println("Ihre Antwort (1-4):");
                choice = scan.nextInt();

                answers.add(choice);

                int indexRightAnswer = 0;
                for (int k = 0; k < 4; k++){
                    if (randQuestion.getAnswers().get(k).getCorrectness()){
                        indexRightAnswer = k;
                    }
                }

                Answer rightAnswer = randQuestion.getAnswers().get(indexRightAnswer);

                if (rightAnswer.getCorrectness() ==
                        randQuestion.getAnswers().get(choice - 1).getCorrectness()) {
                    System.out.println("Diese Antwort war richtig!!!");
                    correct++;
                } else {
                    System.out.println("Dies war die falsche Antwort!" +
                            "\nDie richtige Antwort lautet: " + rightAnswer.getDescription());
                }
            }
        }

        Date end = new Date();
        gs.setEnd(end);

        System.out.println("Spieler: " + playername + " konnte " + correct + " Punkte von "
                + (numberOfCategories * numberOfQuestions) + " erzielen!!!");

        player.addGame(gs);
        em.persist(player);
        em.persist(gs);


        gs.setCorrectAnswers(correct);

        System.out.println("\nNoch ne Runde????(j | n)");

        em.getTransaction().commit();

        String again = scan.next();

        if (again.equals("j")) {
            play();
        } else {
            printStart();
        }
    }

    public void printCategories(){
        /*Set<String> category = categories.keySet();
        System.out.println(category.toString());*/

        for (int i = 0; i < catessssss.size(); i++){
            System.out.println((i + 1) + ". " + catessssss.get(i).getName());
        }
    }

    public void getCategorysFromDB(){
        emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();

        Query q = em.createQuery("select c from Category c");
        catessssss = q.getResultList();

        /*for(int i = 0; i < catessssss.size();i++){
            System.out.println(catessssss.get(i).getName());
        }*/
    }

    public Player getPlayersFromDB(String pname){

        emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();

        boolean isRegistered = false;
        Player player = null;
        int playerID;

        em.getTransaction().begin();

        Query q = em.createQuery("SELECT p FROM Player p");

        List<Player> registeredPlayers = q.getResultList();

        for (Player p : registeredPlayers){
            if (p.getName().equals((pname))) {
                playerID = p.getpID();
                player = em.find(Player.class, playerID);
                isRegistered = true;
                break;
            }
        }

        if (!isRegistered){
            player = new Player(pname);
            em.persist(player);
        }

        return player;
    }

    public void simulate(){
        Simulation sim = new Simulation();
        sim.simulate();
    }


}
