package use_case.gameplay;

import entity.GameState;
import entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameplayInteractorTest {

    private static class FakePresenter implements GameplayOutputBoundary {
        GameplayOutputData lastAnswerResult;
        GameplayOutputData lastQuestionPresented;
        int answerResultCallCount = 0;
        int questionPresentCallCount = 0;

        @Override
        public void presentAnswerResult(GameplayOutputData outputData) {
            this.lastAnswerResult = outputData;
            this.answerResultCallCount++;
        }

        @Override
        public void presentQuestion(GameplayOutputData outputData) {
            this.lastQuestionPresented = outputData;
            this.questionPresentCallCount++;
        }
    }

    private GameState gameState;
    private FakePresenter presenter;
    private GameplayInteractor interactor;
    private List<Question> testQuestions;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        presenter = new FakePresenter();
        interactor = new GameplayInteractor(gameState, presenter);

        // Create test questions
        testQuestions = new ArrayList<>();
        
        // Question 1: Easy question with correct answer at index 0
        ArrayList<String> choices1 = new ArrayList<>();
        choices1.add("Correct Answer");
        choices1.add("Wrong Answer 1");
        choices1.add("Wrong Answer 2");
        choices1.add("Wrong Answer 3");
        testQuestions.add(new Question("multiple", "What is 2+2?", choices1, 0, 100));

        // Question 2: Medium question with correct answer at index 2
        ArrayList<String> choices2 = new ArrayList<>();
        choices2.add("Wrong Answer 1");
        choices2.add("Wrong Answer 2");
        choices2.add("Correct Answer");
        choices2.add("Wrong Answer 3");
        testQuestions.add(new Question("multiple", "What is the capital of France?", choices2, 2, 200));

        // Question 3: Hard question (True/False)
        ArrayList<String> choices3 = new ArrayList<>();
        choices3.add("True");
        choices3.add("False");
        testQuestions.add(new Question("boolean", "The Earth is round.", choices3, 0, 300));

        gameState.setQuestions(testQuestions);
        gameState.reset();
    }

    @Test
    void testSubmitAnswer_CorrectAnswer_AddsScore() {
        // Arrange: Answer the first question correctly
        GameplayInputData inputData = new GameplayInputData(0); // Correct index for first question

        // Act
        interactor.submitAnswer(inputData);

        // Assert: Score should be added
        assertEquals(100, gameState.getScore(), "Score should be 100 after correct answer");
        assertEquals(1, presenter.answerResultCallCount, "Presenter should be called once");
        assertNotNull(presenter.lastAnswerResult, "Presenter should receive output data");
        assertTrue(presenter.lastAnswerResult.isCorrect(), "Answer should be marked as correct");
        assertEquals(100, presenter.lastAnswerResult.getQuestionScore(), "Question score should be 100");
        assertEquals(100, presenter.lastAnswerResult.getTotalScore(), "Total score should be 100");
        assertTrue(presenter.lastAnswerResult.hasMoreQuestions(), "Should have more questions");
    }

    @Test
    void testSubmitAnswer_IncorrectAnswer_NoScoreAdded() {
        // Arrange: Answer the first question incorrectly
        GameplayInputData inputData = new GameplayInputData(1); // Wrong index for first question

        // Act
        interactor.submitAnswer(inputData);

        // Assert: Score should not be added
        assertEquals(0, gameState.getScore(), "Score should remain 0 after incorrect answer");
        assertFalse(presenter.lastAnswerResult.isCorrect(), "Answer should be marked as incorrect");
        assertEquals(0, presenter.lastAnswerResult.getQuestionScore(), "Question score should be 0");
        assertEquals(0, presenter.lastAnswerResult.getTotalScore(), "Total score should remain 0");
    }

    @Test
    void testSubmitAnswer_MovesToNextQuestion() {
        // Arrange: Answer first question
        GameplayInputData inputData = new GameplayInputData(0);

        // Act
        interactor.submitAnswer(inputData);

        // Assert: Question index should advance
        assertEquals(1, gameState.getCurrentQuestionIndex(), 
                "Question index should advance to 1 after answering");
        assertEquals(testQuestions.get(1), gameState.getCurrentQuestion(),
                "Current question should be the second question");
    }

    @Test
    void testSubmitAnswer_MultipleQuestions_ScoreAccumulates() {
        // Arrange & Act: Answer first question correctly
        interactor.submitAnswer(new GameplayInputData(0)); // Correct: index 0
        assertEquals(100, gameState.getScore(), "Score after first question should be 100");

        // Answer second question correctly (correct index is 2)
        interactor.submitAnswer(new GameplayInputData(2)); // Correct: index 2
        
        // Assert: Score should accumulate
        assertEquals(300, gameState.getScore(), "Score should be 300 (100 + 200) after two correct answers");
        assertEquals(2, presenter.answerResultCallCount, "Presenter should be called twice");
        assertEquals(300, presenter.lastAnswerResult.getTotalScore(), 
                "Total score in output should be 300");
    }

    @Test
    void testSubmitAnswer_MixedCorrectIncorrect_OnlyCorrectAddsScore() {
        // Arrange & Act: Answer first question correctly
        interactor.submitAnswer(new GameplayInputData(0)); // Correct
        assertEquals(100, gameState.getScore());

        // Answer second question incorrectly
        interactor.submitAnswer(new GameplayInputData(0)); // Wrong (correct is index 2)

        // Assert: Score should only include first question
        assertEquals(100, gameState.getScore(), 
                "Score should remain 100 after one correct and one incorrect answer");
        assertFalse(presenter.lastAnswerResult.isCorrect(), 
                "Last answer should be marked as incorrect");
    }

    @Test
    void testSubmitAnswer_LastQuestion_NoMoreQuestionsFlag() {
        // Arrange: Answer first two questions to get to the last one
        interactor.submitAnswer(new GameplayInputData(0));
        interactor.submitAnswer(new GameplayInputData(2));

        // Act: Answer the last question (index 0 for True/False)
        interactor.submitAnswer(new GameplayInputData(0));

        // Assert: Should indicate no more questions
        assertFalse(presenter.lastAnswerResult.hasMoreQuestions(), 
                "Should indicate no more questions after last question");
        assertEquals(3, gameState.getCurrentQuestionIndex(), 
                "Should be at index 3 (beyond the last question)");
    }

    @Test
    void testGetNextQuestion_ReturnsCurrentQuestion() {
        // Act
        interactor.getNextQuestion();

        // Assert: Presenter should receive the first question
        assertEquals(1, presenter.questionPresentCallCount, 
                "Presenter should be called once");
        assertNotNull(presenter.lastQuestionPresented, 
                "Presenter should receive output data");
        assertEquals(testQuestions.get(0), presenter.lastQuestionPresented.getCurrentQuestion(),
                "Should present the first question");
        assertEquals(0, presenter.lastQuestionPresented.getTotalScore(),
                "Initial score should be 0");
        assertTrue(presenter.lastQuestionPresented.hasMoreQuestions(),
                "Should indicate more questions available");
    }

    @Test
    void testGetNextQuestion_AfterAnswering_ReturnsNextQuestion() {
        // Arrange: Answer first question
        interactor.submitAnswer(new GameplayInputData(0));

        // Act: Get next question
        interactor.getNextQuestion();

        // Assert: Should present the second question
        assertEquals(testQuestions.get(1), presenter.lastQuestionPresented.getCurrentQuestion(),
                "Should present the second question");
        assertEquals(100, presenter.lastQuestionPresented.getTotalScore(),
                "Score should reflect previous correct answer");
    }

    @Test
    void testGetNextQuestion_NoMoreQuestions_DoesNotCrash() {
        // Arrange: Answer all questions
        interactor.submitAnswer(new GameplayInputData(0)); // Question 1
        interactor.submitAnswer(new GameplayInputData(2)); // Question 2
        interactor.submitAnswer(new GameplayInputData(0)); // Question 3 (last)

        // Reset question present call count
        presenter.questionPresentCallCount = 0;

        // Act: Try to get next question when none remain
        interactor.getNextQuestion();

        // Assert: Should not crash, but also should not present anything
        // (Based on the implementation, it returns early if no more questions)
        // This test ensures the method handles the edge case gracefully
        assertTrue(gameState.getCurrentQuestionIndex() >= testQuestions.size(),
                "Question index should be beyond available questions");
    }

    @Test
    void testSubmitAnswer_OutputDataContainsCorrectQuestion() {
        // Arrange
        GameplayInputData inputData = new GameplayInputData(0);

        // Act
        interactor.submitAnswer(inputData);

        // Assert: Output should contain the question that was answered
        assertNotNull(presenter.lastAnswerResult.getCurrentQuestion(),
                "Output should contain the answered question");
        assertEquals(testQuestions.get(0), presenter.lastAnswerResult.getCurrentQuestion(),
                "Output should contain the first question");
    }

    @Test
    void testGetNextQuestion_WithQuestions_AlwaysTrueHasMoreQuestions() {
        // Test at different question indices
        interactor.getNextQuestion();
        assertTrue(presenter.lastQuestionPresented.hasMoreQuestions(), 
                "Should have more questions at start");

        interactor.submitAnswer(new GameplayInputData(0));
        interactor.getNextQuestion();
        assertTrue(presenter.lastQuestionPresented.hasMoreQuestions(), 
                "Should have more questions after first answer");

        interactor.submitAnswer(new GameplayInputData(2));
        interactor.getNextQuestion();
        assertTrue(presenter.lastQuestionPresented.hasMoreQuestions(), 
                "Should have more questions before last answer");
    }
}

