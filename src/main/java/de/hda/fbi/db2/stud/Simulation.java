package de.hda.fbi.db2.stud;


import java.time.LocalDate;
import java.util.Collections;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 * Simulation und Generierung der Massendaten.
 *
 * @author Albin Latifi
 *
 * @version 1.0
 */
public class Simulation {

    private EntityManager em;
    private Gamesession gs;
    private List<Category> categories;
    private List<Question> questions;
    private List<Answer> answers;
    private int playerID = 0;
    private int counter;
    LocalDate randomDate;
    int startCalender = (int) LocalDate.of(2018, 1, 1).toEpochDay();
    int endCalender = (int) LocalDate.now().toEpochDay();



    public Simulation(){
        gs = new Gamesession();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();
    }

    public void simulate(){
        Query query = em.createQuery("select c from Category c");
        categories = query.getResultList();

        List<Player> players = new ArrayList<>();

       em.getTransaction().begin();

        for (int i = 0; i < 10000; i++){
            Player player = getPlayerFromDB();
            players.add(player);
            //em.persist(player);
        }
        em.getTransaction().commit();

        System.out.println("Alle Spieler commited");

        int anzahlSpiele = 100;

        em.getTransaction().begin();
        for (Player p : players){
            for (int i = 0; i < anzahlSpiele; i++){
                Gamesession gs = new Gamesession();
                gs.setPlayer(p);

                Collections.shuffle(categories);
                gs.setSelectedCategories(categories.subList(0, 2));

                for (Category c : gs.getSelectedCategories()){
                    questions = c.getQuestions();
                    Collections.shuffle(questions);

                    int maxQuestions = 3;

                    if (questions.size() < maxQuestions){
                        questions = questions.subList(0, questions.size());
                    } else {
                        questions = questions.subList(0, maxQuestions);
                    }
                    gs.setAskedQuestions(questions);
                    gs.setNumberOfQuestion(3 * questions.size());

                    Random rand = new Random();

                    long randomDay = startCalender + rand.nextInt(endCalender - startCalender);
                    randomDate = LocalDate.ofEpochDay(randomDay);

                    gs.setStart(Date.from(randomDate.atTime(LocalTime.now())
                            .atZone(ZoneId.systemDefault()).toInstant()));

                    for (Question q : questions){


                        answers = q.getAnswers();
                        int randomChoice = rand.ints(0, 4).limit(1).findFirst().getAsInt();
                        Answer choice = answers.get(randomChoice);

                        if (getCorrectAnswer(q, randomChoice)){
                            gs.addToCorrectCounter();
                        }
                    }

                    gs.setAskedQuestions(questions);
                    gs.setEnd(Date.from(randomDate.atTime(LocalTime.now())
                            .atZone(ZoneId.systemDefault()).toInstant()));
                    em.persist(gs);
                    counter++;

                    if (counter == 2000000) {
                        //System.out.println("Games: " + counter);
                        em.getTransaction().commit();
                        //em.clear();
                        em.getTransaction().begin();
                        counter = 0;
                    }
                }

            }

        }
        if (em.getTransaction().isActive()){
            em.getTransaction().commit();
        }
    }

    private String generatePlayerName(){
        playerID++;
        String playername = "player" + playerID;

        return playername;
    }

    private Player getPlayerFromDB(){

        String name = generatePlayerName();

        Query q = em.createQuery("select p from Player p where p.name =:playername")
                .setParameter("playername", name);

        Player player;

        if (q.getResultList().isEmpty()){
            player = new Player(name);
        } else {
            player = (Player) q.getSingleResult();
        }


        return player;
    }

    private boolean getCorrectAnswer(Question question, int index){
        boolean correct = false;
        for (int i = 0; i < 4; i++){
            if (question.getAnswers().get(index).getCorrectness()){
                correct = true;
            } else {
                correct = false;
            }
        }
        return correct;
    }

}
