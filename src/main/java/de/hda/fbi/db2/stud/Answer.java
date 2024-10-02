package de.hda.fbi.db2.stud;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;



/**
 * speichert Antworten und verwaltet diese.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */


@Entity
@Table(name = "Answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int aID;
    private String description;
    private boolean isRight = false;
    @OneToOne
    private Question question;

    public Answer(){};

    public String getDescription(){
        return description;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public int getId(){
        return aID;
    }

    public void setID(int id){
        this.aID = id;
    }

    public void setCorrectness(boolean correct){
        this.isRight = correct;
    }

    public boolean getCorrectness(){
        return isRight;
    }

    public void setQuestion(Question q){
        this.question = q;
    }

    public Question getQuestion(){
        return question;
    }



}
