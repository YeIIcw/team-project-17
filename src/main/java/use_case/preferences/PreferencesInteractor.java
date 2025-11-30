package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferencesInteractor implements PreferencesInputBoundary {

    private final QuestionFetcher questionFetcher;
    private final GameState gameState;
    private final PreferencesOutputBoundary presenter;
    private final Map<String, Integer> categoryMap;

    public PreferencesInteractor(QuestionFetcher questionFetcher,
                                 GameState gameState,
                                 PreferencesOutputBoundary presenter,
                                 Map<String, Integer> categoryMap) {
        this.questionFetcher = questionFetcher;
        this.gameState = gameState;
        this.presenter = presenter;
        this.categoryMap = categoryMap;
    }

    @Override
    public void execute(PreferencesInputData input) {

        int categoryId = mapCategoryNameToId(input.getCategory());
        String difficultyParam = input.getDifficulty().toLowerCase(); // "easy"
        String typeParam = input.getType().equals("Multiple Choice") ? "multiple" : "boolean";
        int amount = 5;

        try {
            List<Question> questions = questionFetcher.getQuestions(
                    String.valueOf(categoryId),
                    difficultyParam,
                    typeParam,
                    amount
            );

            gameState.reset(); // Reset score and question index for new game
            gameState.setQuestions(questions);

            PreferencesOutputData outputData = new PreferencesOutputData(
                    input.getCategory(),
                    input.getDifficulty(),
                    input.getType(),
                    input.getNumQuestions(),
                    true,
                    null
            );
            presenter.present(outputData);

        } catch (QuestionFetcher.QuestionNotFoundException e) {
            PreferencesOutputData outputData = new PreferencesOutputData(
                    input.getCategory(),
                    input.getDifficulty(),
                    input.getType(),
                    input.getNumQuestions(),
                    false,
                    "Could not load questions. Please try again."
            );
            presenter.present(outputData);
        }
    }

    private int mapCategoryNameToId(String categoryName) {
        Integer id = categoryMap.get(categoryName);
        if (id == null) {
            throw new IllegalArgumentException("Unknown category: " + categoryName);
        }
        return id;
    }
}
