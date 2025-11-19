package use_case.preferences;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import entity.Question;
import use_case.login.LoginInteractor;

public class PreferencesInteractor implements PreferencesInputBoundary {

    private final QuestionFetcher questionFetcher;
    private final GameState gameState;

    private static final Map<String, Integer> CATEGORY_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put("General Knowledge", 9);
        CATEGORY_MAP.put("Entertainment: Books", 10);
        CATEGORY_MAP.put("Entertainment: Film", 11);
        CATEGORY_MAP.put("Entertainment: Music", 12);
        CATEGORY_MAP.put("Entertainment: Musicals & Theatres", 13);
        CATEGORY_MAP.put("Entertainment: Television", 14);
        CATEGORY_MAP.put("Entertainment: Video Games", 15);
        CATEGORY_MAP.put("Entertainment: Board Games", 16);
        CATEGORY_MAP.put("Science & Nature", 17);
        CATEGORY_MAP.put("Science: Computers", 18);
        CATEGORY_MAP.put("Science: Mathematics", 19);
        CATEGORY_MAP.put("Mythology", 20);
        CATEGORY_MAP.put("Sports", 21);
        CATEGORY_MAP.put("Geography", 22);
        CATEGORY_MAP.put("History", 23);
        CATEGORY_MAP.put("Politics", 24);
        CATEGORY_MAP.put("Art", 25);
        CATEGORY_MAP.put("Celebrities", 26);
        CATEGORY_MAP.put("Animals", 27);
        CATEGORY_MAP.put("Vehicles", 28);
        CATEGORY_MAP.put("Entertainment: Comics", 29);
        CATEGORY_MAP.put("Science: Gadgets", 30);
        CATEGORY_MAP.put("Entertainment: Japanese Anime & Manga", 31);
        CATEGORY_MAP.put("Entertainment: Cartoon & Animations", 32);
    }

    public PreferencesInteractor(QuestionFetcher questionFetcher, GameState gameState) {
        this.questionFetcher = questionFetcher;
        this.gameState = gameState;
    }

    @Override
    public void applyPreferences(PreferencesInputData input) {
        // Map GUI strings → API values
        String category = input.getCategory();
        int categoryId          = mapCategoryNameToId(category);
        String difficultyParam  = input.getDifficulty().toLowerCase();
        String typeParam        = input.getType().equals("Multiple Choice") ? "multiple" : "boolean";
        int amount              = input.getNumQuestions();

        try {
            List<Question> questions = questionFetcher.getQuestions(
                    String.valueOf(categoryId),
                    difficultyParam,
                    typeParam,
                    amount
            );

            gameState.setQuestions(questions);

        } catch (QuestionFetcher.QuestionNotFoundException e) {
        }
    }

    private int mapCategoryNameToId(String categoryName) {
        Integer id = CATEGORY_MAP.get(categoryName);
        if (id == null) {
            throw new IllegalArgumentException("Unknown category: " + categoryName);
        }
        return id;
    }
}
