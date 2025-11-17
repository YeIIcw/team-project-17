package interface_adapter.Preferences;

import interface_adapter.Login.LoginController;
import use_case.preferences.PreferencesInteractor;

public class PreferencesController {
    private final String category;
    private final String difficulty;
    private final String type;
    private final int numQuestions;

    public PreferencesController(String category, String difficulty, String type, int numQuestions) {
        this.category = category;
        this.difficulty = difficulty;
        this.type = type;
        this.numQuestions = numQuestions;
    }

    public void execute() {
        LoginController loginController = new LoginController();
//        loginController.execute();
        loginController.setPreferences(category, difficulty, type, numQuestions);
        //do something to move to the first question view
    }
}
