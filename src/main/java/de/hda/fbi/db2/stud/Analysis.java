package de.hda.fbi.db2.stud;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Analyse der Daten.
 *
 * @author Albin Latifi
 *
 * @version 1.0
 */

public class Analysis {

    private EntityManager em;


    public Analysis(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgresPU");
        em = emf.createEntityManager();
    }

    public void chooseAnalyse(){
        Scanner scanner = new Scanner(System.in, ("utf-8"));

        System.out.println("Welche Analyseergebnisse wollen Sie sehen?");
        System.out.println("1:Aufgabe 1\n2:Aufgabe 2\n3:Aufgabe 3\n4:Aufgabe 4");

        int choice = scanner.nextInt();

        switch (choice){
            case 1:
                getPeriodFromUser();
                chooseAnalyse();
                break;
            case 2:
                getPlayerNameToAnalyze();
                chooseAnalyse();
                break;
            case 3:
                getNumberOfGames();
                chooseAnalyse();
                break;
            case 4:
                getMostPlayedCategories();
                chooseAnalyse();
                break;
            default:
                break;
        }





    }


    public void getPeriodFromUser(){

        Scanner scanner = new Scanner(System.in, "utf-8");

        System.out.println("Aufgabe 1:");
        System.out.println("Geben Sie das Startdatum des gewünschten Zeitraums an: (dd.mm.yyyy)");

        String startString = scanner.nextLine();

        System.out.println("Geben Sie das Enddatum des gewünschten Zeitraums an: (dd.mm.yyyy)");

        String endString = scanner.nextLine();

        try {
            Date start = new SimpleDateFormat("dd.MM.yyyy").parse(startString);
            Date end = new SimpleDateFormat("dd.MM.yyyy").parse(endString);
            Timestamp startTimeStamp = new Timestamp(start.getTime());
            Timestamp endTimeStamp = new Timestamp(end.getTime());
            getPlayerInPeriod(startTimeStamp, endTimeStamp);
        } catch (ParseException p){
            System.out.println("Fehler in der Eingabe");
        }
    }

    public void getPlayerInPeriod(Timestamp start, Timestamp end){

        Query q = em.createQuery("SELECT gs.player.name FROM Gamesession AS gs" +
                " WHERE gs.timeStampStart > :startdate AND gs.timeStampEnd < :enddate");

        List names = q.setParameter("startdate", start).setParameter("enddate", end)
                .getResultList();

        if (!names.isEmpty()){
            Iterator it = names.iterator();

            System.out.println("Alle Spieler im gewünschten Zeitraum: ");

            while (it.hasNext()){
                String playername = it.next().toString();
                System.out.println("Spieler: " + playername);
            }
        } else {
            System.out.println("In diesem Zeitraum hat kein Spieler gespielt!");
        }
    }

    public void getPlayerNameToAnalyze(){
        Scanner scanner = new Scanner(System.in, "utf-8");
        String name;
        System.out.println("Geben Sie den Namen des zu analysierenden Spielers ein:");

        name = scanner.nextLine();
        getDataForGivenPlayer(name);

    }

    public void getDataForGivenPlayer(String name){
        Query q = em.createQuery(
                "SELECT gs.gsid, gs.timeStampEnd, gs.timeStampEnd, gs.correctAnswers " +
                " , COUNT(gs.askedQuestions) FROM Gamesession AS gs WHERE gs.player.name = " +
                        ":playername GROUP BY gs.gsid");

        List playerdata = q.setParameter("playername", name).getResultList();

        if (!playerdata.isEmpty()){
            Iterator it = playerdata.iterator();

            System.out.println("Spielerdaten:");

            while (it.hasNext()){
                Object[] record = (Object[]) it.next();

                System.out.println("Gamesession ID              : " + record[0]);
                System.out.println("Startzeit                   : " + record[1]);
                System.out.println("Endzeit                     : " + record[2]);
                System.out.println("Anzahl richtiger Antworten  : " + record[3]);
                System.out.println("Anzahl gestellter Fragen    : " + record[4]);
            }
        } else {
            System.out.println("Es ist kein Spieler mit dem Namen" + name + "registriert!");
        }

    }

    public void getNumberOfGames(){

        Query q = em.createQuery(
                "SELECT gs.player.name, count(gs.gsid) from Gamesession as gs " +
                        " GROUP BY gs.player.name order by count(gs.gsid) desc");

        List playerList = q.getResultList();

        if (!playerList.isEmpty()){

            Iterator it = playerList.iterator();

            while (it.hasNext()){
                Object[] record = (Object[]) it.next();
                System.out.println("Spieler : " + record[0] + " Spiele: " + record[1]);
            }

        } else {
            System.out.println("Es sind noch keine Spieler registriert!!");
        }

    }

    public void getMostPlayedCategories(){
        Query q = em.createQuery(
                "SELECT c1.name, count(gs.gsid) from Gamesession gs, Category c1 " +
                        "where c1 member of gs.selectedCategories group by c1.name order by " +
                        "count(gs.gsid) desc");


        List categories = q.getResultList();

        if (!categories.isEmpty()){
            Iterator it = categories.iterator();

            while (it.hasNext()){
                Object[] record = (Object[]) it.next();
                System.out.println("Kategorie: " + record[0] + " Spiele: " + record[1]);
            }

        } else {
            System.out.println("Es wurden noch keine Kategorien gespielt!");
        }
    }


}

