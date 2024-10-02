package de.hda.fbi.db2.stud;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;






/**
 * speichert die Kategorien und verwaltet diese.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category {
    //automatisch generieren lassen
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cID;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Question> questions = new ArrayList<>();

    public Category(){};

    public List<Question> getQuestions(){
        return questions;
    }

    public int getID(){
        return cID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addQuestion(Question quest){
         questions.add(quest);
    }
}
