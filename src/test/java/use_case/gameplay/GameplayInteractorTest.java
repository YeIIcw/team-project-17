package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PreferencesInteractorTest4 {

    private PreferencesInteractor interactor;
    private FakeQuestionFetcher fakeQuestionFetcher;
    private TestGameState testGameState;
    private TestPresenter testPresenter;
    private Map<String, Integer> categoryMap;

    @BeforeEach
    void setUp() {
        fakeQuestionFetcher = new FakeQuestionFetcher();
        testGameState = new TestGameState();
        testPresenter = new TestPresenter();

        categoryMap = new HashMap<>();
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("History", 23);
        categoryMap.put("Science & Nature", 17);

        interactor = new PreferencesInteractor(
                fakeQuestionFetcher,
                testGameState,
                testPresenter,
                categoryMap
        );
    }

    @Test
    void testExecute_Success() {
        // Arrange
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge",
                "Easy",
                "Multiple Choice",
                10
        );

        fakeQuestionFetcher.setShouldSucceed(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(testPresenter.wasCalledWithSuccess);
        assertEquals("General Knowledge", testPresenter.lastOutputData.getCategory());
        assertEquals("Easy", testPresenter.lastOutputData.getDifficulty());
        assertEquals("Multiple Choice", testPresenter.lastOutputData.getType());
        assertEquals(10, testPresenter.lastOutputData.getNumQuestions());
        assertTrue(testPresenter.lastOutputData.isSuccess());
        assertNull(testPresenter.lastOutputData.getMessage());
        assertTrue(testGameState.wasResetCalled);
        assertNotNull(testGameState.storedQuestions);
    }

    @Test
    void testExecute_QuestionNotFoundException() {
        // Arrange
        PreferencesInputData inputData = new PreferencesInputData(
                "History",
                "Hard",
                "True/False",
                20
        );

        fakeQuestionFetcher.setShouldThrowNotFoundException(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(testPresenter.lastOutputData.isSuccess());
        assertEquals("Could not load questions. Please try again.",
                testPresenter.lastOutputData.getMessage());
        assertNull(testGameState.storedQuestions);
    }

    @Test
    void testExecute_GenericException() {
        // Arrange
        PreferencesInputData inputData = new PreferencesInputData(
                "Science & Nature",
                "Medium",
                "Multiple Choice",
                15
        );

        fakeQuestionFetcher.setShouldThrowGenericException(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse(testPresenter.lastOutputData.isSuccess());
        assertTrue(testPresenter.lastOutputData.getMessage().contains("An error occurred"));
    }

    @Test
    void testExecute_UnknownCategory() {
        // Arrange
        PreferencesInputData inputData = new PreferencesInputData(
                "Unknown Category",
                "Easy",
                "Multiple Choice",
                10
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            interactor.execute(inputData);
        });
    }

    @Test
    void testExecute_TrueFalseTypeConversion() {
        // Arrange
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge",
                "Easy",
                "True/False",
                5
        );

        fakeQuestionFetcher.setShouldSucceed(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertEquals("9", fakeQuestionFetcher.lastCategory);
        assertEquals("easy", fakeQuestionFetcher.lastDifficulty);
        assertEquals("boolean", fakeQuestionFetcher.lastType);  // Converted!
        assertEquals(5, fakeQuestionFetcher.lastNumQuestions);
    }

    @Test
    void testExecute_DifficultyConversion() {
        // Test Easy
        testDifficultyConversion("Easy", "easy");

        // Test Medium
        testDifficultyConversion("Medium", "medium");

        // Test Hard
        testDifficultyConversion("Hard", "hard");
    }

    private void testDifficultyConversion(String input, String expected) {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge",
                input,
                "Multiple Choice",
                10
        );

        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals(expected, fakeQuestionFetcher.lastDifficulty);
    }

    // ========================================
    // TEST DOUBLES (Fake implementations)
    // ========================================

    /**
     * Fake QuestionFetcher for testing
     */
    private static class FakeQuestionFetcher implements QuestionFetcher {
        private boolean shouldSucceed = true;
        private boolean shouldThrowNotFoundException = false;
        private boolean shouldThrowGenericException = false;

        // Track what was called
        String lastCategory;
        String lastDifficulty;
        String lastType;
        int lastNumQuestions;

        void setShouldSucceed(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
            this.shouldThrowNotFoundException = false;
            this.shouldThrowGenericException = false;
        }

        void setShouldThrowNotFoundException(boolean shouldThrow) {
            this.shouldThrowNotFoundException = shouldThrow;
            this.shouldSucceed = false;
            this.shouldThrowGenericException = false;
        }

        void setShouldThrowGenericException(boolean shouldThrow) {
            this.shouldThrowGenericException = shouldThrow;
            this.shouldSucceed = false;
            this.shouldThrowNotFoundException = false;
        }

        @Override
        public List<Question> getQuestions(String category, String difficulty,
                                           String type, int numQuestions)
                throws QuestionNotFoundException {
            // Record what was called
            this.lastCategory = category;
            this.lastDifficulty = difficulty;
            this.lastType = type;
            this.lastNumQuestions = numQuestions;

            if (shouldThrowNotFoundException) {
                throw new QuestionNotFoundException();
            }

            if (shouldThrowGenericException) {
                throw new RuntimeException("Network error");
            }

            // Return fake questions
            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < numQuestions; i++) {
                ArrayList<String> choices = new ArrayList<>();
                choices.add("Answer A");
                choices.add("Answer B");
                choices.add("Answer C");
                choices.add("Answer D");

                Question question = new Question(
                        type,
                        "Test Question " + i,
                        choices,
                        0,
                        100
                );
                questions.add(question);
            }
            return questions;
        }
    }

    /**
     * Test GameState to track interactions
     */
    private static class TestGameState extends GameState {
        boolean wasResetCalled = false;
        List<Question> storedQuestions = null;

        @Override
        public void reset() {
            wasResetCalled = true;
            super.reset();
        }

        @Override
        public void setQuestions(List<Question> questions) {
            storedQuestions = questions;
            super.setQuestions(questions);
        }
    }

    /**
     * Test Presenter to capture output
     */
    private static class TestPresenter implements PreferencesOutputBoundary {
        boolean wasCalledWithSuccess = false;
        PreferencesOutputData lastOutputData = null;

        @Override
        public void present(PreferencesOutputData outputData) {
            this.lastOutputData = outputData;
            this.wasCalledWithSuccess = outputData.isSuccess();
        }
    }
}