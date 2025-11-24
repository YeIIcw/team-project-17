package use_case.preferences;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;

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
        System.out.println("DEBUG: PreferencesInteractor - execute() called");
        System.out.println("  Category: " + input.getCategory());
        System.out.println("  Difficulty: " + input.getDifficulty());
        System.out.println("  Type: " + input.getType());
        System.out.println("  NumQuestions: " + input.getNumQuestions());
        
        int categoryId = mapCategoryNameToId(input.getCategory());
        String difficultyParam = input.getDifficulty().toLowerCase(); // "easy"
        String typeParam = input.getType().equals("Multiple Choice") ? "multiple" : "boolean";
        int amount = input.getNumQuestions();

        System.out.println("DEBUG: PreferencesInteractor - Mapped parameters:");
        System.out.println("  Category ID: " + categoryId);
        System.out.println("  Difficulty param: " + difficultyParam);
        System.out.println("  Type param: " + typeParam);
        System.out.println("  Amount: " + amount);

        try {
            System.out.println("DEBUG: PreferencesInteractor - Fetching questions from API...");
            List<Question> questions = questionFetcher.getQuestions(
                    String.valueOf(categoryId),
                    difficultyParam,
                    typeParam,
                    amount
            );
            System.out.println("DEBUG: PreferencesInteractor - Successfully fetched " + questions.size() + " questions");

            gameState.reset(); // Reset score and question index for new game
            gameState.setQuestions(questions);
            System.out.println("DEBUG: PreferencesInteractor - Questions stored in GameState");

            PreferencesOutputData outputData = new PreferencesOutputData(
                    input.getCategory(),
                    input.getDifficulty(),
                    input.getType(),
                    input.getNumQuestions(),
                    true,
                    null
            );
            System.out.println("DEBUG: PreferencesInteractor - Calling presenter.present() with success=true");
            presenter.present(outputData);

        } catch (QuestionFetcher.QuestionNotFoundException e) {
            System.out.println("ERROR: PreferencesInteractor - QuestionNotFoundException: " + e.getMessage());
            PreferencesOutputData outputData = new PreferencesOutputData(
                    input.getCategory(),
                    input.getDifficulty(),
                    input.getType(),
                    input.getNumQuestions(),
                    false,
                    "Could not load questions. Please try again."
            );
            System.out.println("DEBUG: PreferencesInteractor - Calling presenter.present() with success=false");
            presenter.present(outputData);
        } catch (Exception e) {
            System.out.println("ERROR: PreferencesInteractor - Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            PreferencesOutputData outputData = new PreferencesOutputData(
                    input.getCategory(),
                    input.getDifficulty(),
                    input.getType(),
                    input.getNumQuestions(),
                    false,
                    "An error occurred: " + e.getMessage()
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
