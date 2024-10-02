package de.hda.fbi.db2.stud;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;




/**
 * speichert die Fragen und verwaltet diese.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */

@Entity
@Table(name = "Question")
public class Question {
    @Id
    @OrderColumn
    private int qID;
    private String description;
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    private Category category;

    public Question(){};

    public int getID(){
        return qID;
    }

    public void setID(int id){
        this.qID = id;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public String getDescription(){
        return description;
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public void setCategory(Category c){
        this.category = c;
    }

    public Category getCategory(){
        return category;
    }

}
