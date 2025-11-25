package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameState {

//    private final Character character;
//    private final Map gameMap;
    private Position position;

    private List<Question> questions;
    private int currentQuestionIndex;

    private int score;

//    private CombatState currentCombat;         // null when not in combat
//
//    private GamePhase phase;                   // EXPLORATION, QUESTION, COMBAT, GAME_OVER

    public GameState() {
//        this.character = character;
//        this.gameMap = gameMap;
        this.position = new Position(0, 0);
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

    // ❗️ NOTE: already set in PreferencesInteractor by making API calls; thus not in the constructor
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions(){
        return questions;
    }
    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public boolean hasMoreQuestions() {
        return questions != null && currentQuestionIndex < questions.size();
    }

    public void moveToNextQuestion() {
        if (hasMoreQuestions()) {
            currentQuestionIndex++;
        }
    }

    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void reset() {
        this.currentQuestionIndex = 0;
        this.score = 0;
    }

//    public boolean isGameOver() {
//        return phase == GamePhase.GAME_OVER
//                || character.getHealth() <= 0
//                || !hasMoreQuestions();
//    }

    // getters / setters / methods to move player, start combat, etc.
}
