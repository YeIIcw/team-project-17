package use_case.gameplay;

import entity.Question;

public class GameplayOutputData {
    private final boolean isCorrect;
    private final int questionScore;
    private final Question currentQuestion;
    private final int totalScore;
    private final boolean hasMoreQuestions;

    public GameplayOutputData(boolean isCorrect, int questionScore, Question currentQuestion, 
                             int totalScore, boolean hasMoreQuestions) {
        this.isCorrect = isCorrect;
        this.questionScore = questionScore;
        this.currentQuestion = currentQuestion;
        this.totalScore = totalScore;
        this.hasMoreQuestions = hasMoreQuestions;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public int getQuestionScore() {
        return questionScore;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public boolean hasMoreQuestions() {
        return hasMoreQuestions;
    }
}
