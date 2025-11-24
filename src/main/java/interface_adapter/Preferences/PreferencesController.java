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
        System.out.println("DEBUG: PreferencesController - execute() called");
        System.out.println("  Category: " + category + ", Difficulty: " + difficulty + 
                         ", Type: " + type + ", NumQuestions: " + numQuestions);
        
        // Note: Player preferences are stored in Player entity during login
        // We don't need to set them here since they're not used for question fetching
        // The PreferencesInteractor handles the question fetching based on the parameters
        
        PreferencesInputData inputData =
                new PreferencesInputData(category, difficulty, type, numQuestions);
        
        System.out.println("DEBUG: PreferencesController - Calling PreferencesInteractor.execute()");
        interactor.execute(inputData);
        System.out.println("DEBUG: PreferencesController - PreferencesInteractor.execute() completed");
    }
}

