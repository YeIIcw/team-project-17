package entity;

import java.util.ArrayList;

public class Question {
    private String type;
    private String text;
    private ArrayList<String> choices;
    private int correctChoiceIndex;
    private int scoreValue;

    public Question(String type, String text, ArrayList<String> choices, int correctChoiceIndex, int scoreValue) {
        this.text = text;
        this.type = type;
        this.choices = choices;
        this.correctChoiceIndex = correctChoiceIndex;
        this.scoreValue = scoreValue;
    }
}
