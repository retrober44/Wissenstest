package de.hda.fbi.db2.stud;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


import de.hda.fbi.db2.tools.CsvDataReader;

/**
 * Main Class.
 * @version 0.1.1
 * @since 0.1.0
 * @author A. Hofmann
 * @author B.-A. Mokroß
 */


public class Main {
    /**
     * Main Method and Entry-Point.
     * @param args Command-Line Arguments.
     */
    public static void main(String[] args) {

        System.out.println(getGreeting());;
        Game game = new Game();

        game.printStart();
    }

    public static String getGreeting() {
        return "Herzlich Willkommen zum Wissenstest!\n";
    }

}
