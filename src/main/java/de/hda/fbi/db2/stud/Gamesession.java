package de.hda.fbi.db2.stud;

import java.util.*;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;




/**
 * to be anounced.
 *
 * @author Albin Latifi, Rober Köten
 *
 * @version 1.0
 */

@Entity
public class Gamesession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gsid;

    private int numberOfQuestion;

    //@Temporal(TemporalType.TIMESTAMP)
    private Date timeStampStart;

    //@Temporal(TemporalType.TIMESTAMP)
    private Date timeStampEnd;


    int correctAnswers;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Player player;

    @OneToMany
    private List<Question> askedQuestions = new ArrayList<>();
    @ManyToMany
    private List<Category> selectedCategories = new ArrayList<>();


    public Gamesession() {
    }

    public int getGsid() {
        return gsid;
    }

    public int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(int numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    /*public List<Integer> answers() {
        return answers;
    }*/

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Question> getAskedQuestions() {
        return askedQuestions;
    }

    public void setAskedQuestions(List<Question> askedQuestions) {
        this.askedQuestions = askedQuestions;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void addSelectedCategory(Category c){
        selectedCategories.add(c);
    }

    public void addQuestion(Question q){
        askedQuestions.add(q);
    }

    public void setStart(Date start){
        this.timeStampStart = new Date(start.getTime());
    }

    public void setEnd(Date end) {
        this.timeStampEnd = new Date(end.getTime());
    }

    public void setNumberOfQuestions(int noq){
        this.numberOfQuestion = noq;
    }

    public void setCorrectAnswers(int correct){
        this.correctAnswers = correct;
    }

    public void addToCorrectCounter(){
        correctAnswers++;
    }

    public void setAnswers(List<Answer> answers){

    }

}
