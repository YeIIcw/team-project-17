package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.Question;
import entity.GameState;
import interface_adapter.Preferences.PreferencesPresenter;
import interface_adapter.Preferences.PreferencesViewModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PreferencesInteractorTest2 {

  static class MockQuestionFetcher implements QuestionFetcher {
    @Override
    public List<Question> getQuestions(String category, String difficulty, String type, int num) {
      ArrayList<String> choices = new ArrayList<>();
      choices.add("A");
      choices.add("B");
      Question q = new Question("multiple", "Test Q", choices, 0, 1);
      return List.of(q, q, q);
    }
  }

  @Test
  void interactorStoresQuestionsInGameState() {
    GameState state = new GameState();
    PreferencesViewModel vm = new PreferencesViewModel();
    PreferencesPresenter presenter = new PreferencesPresenter(vm);

    PreferencesInteractor interactor = new PreferencesInteractor(new MockQuestionFetcher(), state, presenter,
        Map.of("General Knowledge", 9));

    PreferencesInputData input = new PreferencesInputData("General Knowledge", "easy", "multiple", 3);

    interactor.execute(input);

    assertNotNull(state.getQuestions());
    assertEquals(3, state.getQuestions().size());
    assertEquals("Test Q", state.getQuestions().get(0).getText());
  }
}
