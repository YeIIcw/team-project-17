package entity;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Question> questions = new ArrayList<>();

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}