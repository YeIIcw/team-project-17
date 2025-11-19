package interface_adapter.Preferences;

import entity.Question;
import interface_adapter.Login.LoginController;
import use_case.preferences.PreferencesInputBoundary;
import use_case.preferences.PreferencesInputData;
import use_case.preferences.PreferencesInteractor;

import java.util.ArrayList;

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
        interactor.applyPreferences(inputData);
    }

}
