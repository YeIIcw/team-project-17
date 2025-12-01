package integration;

import app.AppBuilder;
import entity.GameState;
import entity.Question;
import use_case.gameplay.GameplayInputData;
import use_case.gameplay.GameplayInteractor;
import use_case.gameplay.GameplayOutputBoundary;
import use_case.gameplay.GameplayOutputData;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CoreGameIntegrationTest {

    static class MockGameplayPresenter implements GameplayOutputBoundary {
        public Question lastQuestion;
        public boolean lastAnswerCorrect;
        public int lastQuestionScore;
        public int lastTotalScore;
        public boolean lastHasMoreQuestions;

        @Override
        public void presentAnswerResult(GameplayOutputData outputData) {
            this.lastAnswerCorrect = outputData.isCorrect();
            this.lastQuestionScore = outputData.getQuestionScore();
            this.lastTotalScore = outputData.getTotalScore();
            this.lastQuestion = outputData.getCurrentQuestion();
        }

        @Override
        public void presentQuestion(GameplayOutputData outputData) {
            this.lastQuestion = outputData.getCurrentQuestion();
            this.lastHasMoreQuestions = outputData.hasMoreQuestions();
        }
    }

    @Test
    public void testCompleteCoreGameIntegration() {
        System.out.println("Testing Complete Core Game Integration");

        // game start setup
        GameState gameState = new GameState();
        AppBuilder appBuilder = new AppBuilder();
        gameState.setCurrentUsername("TestPlayer");

        // load questions from preferences into game
        Question[] questions = {
                new Question("multiple",
                        "Light attack question?",
                        new ArrayList<>(Arrays.asList("A", "B", "C", "D")),
                        1,
                        100),
                new Question("multiple",
                        "Heavy attack question?",
                        new ArrayList<>(Arrays.asList("A", "B", "C", "D")),
                        2,
                        200)
        };
        gameState.setQuestions(Arrays.asList(questions));
        System.out.println("Preferences loaded " + questions.length + " questions");

        // start gameplay with questions
        MockGameplayPresenter presenter = new MockGameplayPresenter();
        GameplayInteractor gameplayInteractor = new GameplayInteractor(gameState, presenter, appBuilder);

        // answer first combat question (light attack)
        gameplayInteractor.getNextQuestion();
        assertNotNull(presenter.lastQuestion, "Combat should get a question");
        assertEquals("Light attack question?", presenter.lastQuestion.getText());
        System.out.println("Combat received question: " + presenter.lastQuestion.getText());

        // answer gameplay correctly
        gameplayInteractor.submitAnswer(new GameplayInputData(1));
        assertTrue(presenter.lastAnswerCorrect, "Combat move should succeed with correct answer");
        int scoreEarned = presenter.lastQuestionScore;
        assertTrue(scoreEarned > 0, "Successful combat should earn score");
        System.out.println("Combat successful - earned " + scoreEarned + " points");

        assertEquals("TestPlayer", gameState.getCurrentUsername(), "Username preserved");
        assertTrue(gameState.getScore() > 0, "Score accumulated in GameState");
        assertTrue(gameState.hasMoreQuestions(), "Game can continue");

        System.out.println("FINAL: Score=" + gameState.getScore() + ", User=" + gameState.getCurrentUsername());

    }
}
