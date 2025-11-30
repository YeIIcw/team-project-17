package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PreferencesInteractorTest5 {

    private PreferencesInteractor interactor;
    private FakeQuestionFetcher fakeQuestionFetcher;
    private TestGameState testGameState;
    private TestPresenter testPresenter;
    private Map<String, Integer> categoryMap;

    @BeforeEach
    void setUp() {
        // Create instances of our test doubles (defined below)
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
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Easy", "Multiple Choice", 10
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertTrue(testPresenter.wasCalledWithSuccess);
        assertTrue(testPresenter.lastOutputData.isSuccess());
        assertNull(testPresenter.lastOutputData.getMessage());
        assertTrue(testGameState.wasResetCalled);
        assertNotNull(testGameState.storedQuestions);
//        assertEquals(10, testGameState.storedQuestions.size());
    }

    @Test
    void testExecute_QuestionNotFoundException() {
        PreferencesInputData inputData = new PreferencesInputData(
                "History", "Hard", "True/False", 20
        );
        fakeQuestionFetcher.setShouldThrowNotFoundException(true);

        interactor.execute(inputData);

        assertFalse(testPresenter.lastOutputData.isSuccess());
        assertEquals("Could not load questions. Please try again.",
                testPresenter.lastOutputData.getMessage());
    }

//    @Test
//    void testExecute_GenericException() {
//        PreferencesInputData inputData = new PreferencesInputData(
//                "Science & Nature", "Medium", "Multiple Choice", 15
//        );
//        fakeQuestionFetcher.setShouldThrowGenericException(true);
//
//        interactor.execute(inputData);
//
//        assertFalse(testPresenter.lastOutputData.isSuccess());
//        assertTrue(testPresenter.lastOutputData.getMessage().contains("An error occurred"));
//    }

    @Test
    void testExecute_UnknownCategory() {
        PreferencesInputData inputData = new PreferencesInputData(
                "Unknown Category", "Easy", "Multiple Choice", 10
        );

        assertThrows(IllegalArgumentException.class, () -> {
            interactor.execute(inputData);
        });
    }

    @Test
    void testExecute_MultipleChoiceTypeConversion() {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Easy", "Multiple Choice", 5
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals("multiple", fakeQuestionFetcher.lastType);
    }

    @Test
    void testExecute_TrueFalseTypeConversion() {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Easy", "True/False", 5
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals("boolean", fakeQuestionFetcher.lastType);
    }

    @Test
    void testExecute_EasyDifficulty() {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Easy", "Multiple Choice", 10
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals("easy", fakeQuestionFetcher.lastDifficulty);
    }

    @Test
    void testExecute_MediumDifficulty() {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Medium", "Multiple Choice", 10
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals("medium", fakeQuestionFetcher.lastDifficulty);
    }

    @Test
    void testExecute_HardDifficulty() {
        PreferencesInputData inputData = new PreferencesInputData(
                "General Knowledge", "Hard", "Multiple Choice", 10
        );
        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        assertEquals("hard", fakeQuestionFetcher.lastDifficulty);
    }

    // ========================================
    // TEST DOUBLES (Helper Classes)
    // These are defined INSIDE this test file
    // ========================================

    /**
     * Fake implementation of QuestionFetcher for testing.
     * This replaces the real ApiQuestionFetcher so we don't make real API calls.
     */
    private static class FakeQuestionFetcher implements QuestionFetcher {
        private boolean shouldSucceed = true;
        private boolean shouldThrowNotFoundException = false;
        private boolean shouldThrowGenericException = false;

        // Track what was called with
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
            // Record what was called with
            this.lastCategory = category;
            this.lastDifficulty = difficulty;
            this.lastType = type;
            this.lastNumQuestions = numQuestions;

            // Simulate different behaviors
            if (shouldThrowNotFoundException) {
                throw new QuestionNotFoundException();
            }

            if (shouldThrowGenericException) {
                throw new RuntimeException("Simulated network error");
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
                        "Test Question " + i + "?",
                        choices,
                        0,  // correct answer is always index 0
                        100  // score value
                );
                questions.add(question);
            }
            return questions;
        }
    }

    /**
     * Test version of GameState that tracks method calls
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
     * Test implementation of PreferencesOutputBoundary
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

    @Test
    void testExecute_VerifyAllOutputDataFields() {
        PreferencesInputData inputData = new PreferencesInputData(
                "History",
                "Medium",
                "True/False",
                15
        );

        fakeQuestionFetcher.setShouldSucceed(true);

        interactor.execute(inputData);

        PreferencesOutputData outputData = testPresenter.lastOutputData;

        assertNotNull(outputData, "OutputData should not be null");

        assertEquals("History", outputData.getCategory());
        assertEquals("Medium", outputData.getDifficulty());
        assertEquals("True/False", outputData.getType());
        assertEquals(15, outputData.getNumQuestions());
        assertTrue(outputData.isSuccess());
        assertNull(outputData.getMessage());
    }

    @Test
    void testExecute_VerifyErrorOutputDataFields() {
        PreferencesInputData inputData = new PreferencesInputData(
                "Science & Nature",
                "Hard",
                "Multiple Choice",
                20
        );

        fakeQuestionFetcher.setShouldThrowNotFoundException(true);

        interactor.execute(inputData);

        PreferencesOutputData outputData = testPresenter.lastOutputData;

        assertNotNull(outputData, "OutputData should not be null");

        assertEquals("Science & Nature", outputData.getCategory());
        assertEquals("Hard", outputData.getDifficulty());
        assertEquals("Multiple Choice", outputData.getType());
        assertEquals(20, outputData.getNumQuestions());
        assertFalse(outputData.isSuccess());
        assertEquals("Could not load questions. Please try again.", outputData.getMessage());
    }
}