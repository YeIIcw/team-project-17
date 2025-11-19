package interface_adapter.Preferences;

import interface_adapter.Login.LoginController;
import use_case.preferences.PreferencesInputBoundary;
import use_case.preferences.PreferencesInputData;

public class PreferencesController {

    private final PreferencesInputBoundary interactor;

    public PreferencesController(PreferencesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String category, String difficulty, String type, int numQuestions) {
        LoginController loginController = new LoginController();
//        loginController.execute();
        loginController.setPreferences(category, difficulty, type, numQuestions);
        //do something to move to the first question view

        PreferencesInputData inputData =
                new PreferencesInputData(category, difficulty, type, numQuestions);
        interactor.execute(inputData);
    }
}

