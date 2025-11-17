package use_case.preferences;

import use_case.login.LoginInteractor;

public class PreferencesInteractor {

    private final String category;
    private final String difficulty;
    private final String types;
    private final int numQuestions;

    public PreferencesInteractor(String category, String difficulty, String types, int numQuestions) {
        this.category = category;
        this.difficulty = difficulty;
        this.types = types;
        this.numQuestions = numQuestions;
    }

    public void execute() {
        LoginInteractor loginInteractor = new LoginInteractor();
        loginInteractor.setPlayerPreferences(category, difficulty, types, numQuestions);
    }
}
