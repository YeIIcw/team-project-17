package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PreferencesInteractorTest {

    // ---- Fake QuestionFetcher (no network) ----
    private static class FakeQuestionFetcher implements QuestionFetcher {

        boolean called = false;
        List<Question> questionsToReturn = new ArrayList<>();

        @Override
        public List<Question> getQuestions(String category,
                                           String difficulty,
                                           String type,
                                           int numQuestions) throws QuestionNotFoundException {
            called = true;
            // ignore params, just return preconfigured questions
            return questionsToReturn;
        }
    }

    // ---- Fake Presenter ----
    private static class FakePreferencesPresenter implements PreferencesOutputBoundary {

        PreferencesOutputData lastOutput;

        @Override
        public void present(PreferencesOutputData outputData) {
            this.lastOutput = outputData;
        }
    }

    @Test
    void interactorCallsFetcherAndStoresQuestions() {
        // Arrange: fake fetcher returns a single dummy question
        FakeQuestionFetcher fakeFetcher = new FakeQuestionFetcher();
        ArrayList<String> choices = new ArrayList<>();
        choices.add("A");
        choices.add("B");
        Question dummyQuestion = new Question("multiple", "Test question", choices, 0, 1);
        fakeFetcher.questionsToReturn.add(dummyQuestion);

        GameState gameState = new GameState();
        FakePreferencesPresenter presenter = new FakePreferencesPresenter();

        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("General Knowledge", 9);

        PreferencesInputBoundary interactor =
                new PreferencesInteractor(fakeFetcher, gameState, presenter, categoryMap);

        PreferencesInputData inputData =
                new PreferencesInputData("General Knowledge", "Easy", "Multiple Choice", 1);

        // Act
        interactor.execute(inputData);

        // Assert: fetcher was called
        assertTrue(fakeFetcher.called, "QuestionFetcher should have been called");

        // Assert: GameState got the question
        assertNotNull(gameState.getCurrentQuestion(), "GameState should have a current question");
        assertEquals("Test question", gameState.getCurrentQuestion().getText());

        // Assert: presenter got success
        assertNotNull(presenter.lastOutput, "Presenter should have received output data");
        assertTrue(presenter.lastOutput.isSuccess(), "PreferencesOutputData should indicate success");
        assertEquals("General Knowledge", presenter.lastOutput.getCategory());
    }
}
