package interface_adapter.Gameplay;

import entity.Question;

import java.util.List;

public class GameplayViewModel {

    private Question currentQuestion;
    private boolean isCorrect;
    private int questionScore;
    private int totalScore;
    private boolean hasMoreQuestions;
    private String message;

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(int questionScore) {
        this.questionScore = questionScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public boolean hasMoreQuestions() {
        return hasMoreQuestions;
    }

    public void setHasMoreQuestions(boolean hasMoreQuestions) {
        this.hasMoreQuestions = hasMoreQuestions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

