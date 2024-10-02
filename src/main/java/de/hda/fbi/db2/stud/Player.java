package de.hda.fbi.db2.stud;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;



/**
 * to be anounced.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pID;
    private String name;

    @OneToMany(mappedBy = "player")
    private List<Gamesession> gamesessions = new ArrayList<>();

    public Player(){};

    public Player(String name) {
        this.name = name;
    }

    public int getpID() {
        return pID;
    }

    public String getName() {
        return name;
    }

    public void addGame(Gamesession gs){
        gamesessions.add(gs);
    }

}
