package interface_adapter.Login;

import entity.Player;
import interface_adapter.Loggedin.LoggedInController;
import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Preferences.PreferencesController;
import interface_adapter.Preferences.PreferencesViewModel;
import use_case.login.LoginInteractor;
import view.LoggedInView;
import view.PreferencesView;


import java.io.IOException;

public class LoginController {

    private String username;
    private String password;
    private Player player;
    private LoggedInController loggedInController;
    private PreferencesController preferencesController;

    public LoginController() {
        // Default constructor for backward compatibility
    }

    public LoginController(LoggedInController loggedInController, PreferencesController preferencesController) {
        this.loggedInController = loggedInController;
        this.preferencesController = preferencesController;
    }

    public void setLoggedInController(LoggedInController loggedInController) {
        this.loggedInController = loggedInController;
    }

    public void setPreferencesController(PreferencesController preferencesController) {
        this.preferencesController = preferencesController;
    }

    public void execute(String username, String password) throws IOException {
        System.out.println("DEBUG: LoginController - execute() called");
        this.username = username;
        this.password = password;

        LoginInteractor loginInteractor = new LoginInteractor();
        loginInteractor.execute(username, password);

        this.player = loginInteractor.getPlayer();
        System.out.println("DEBUG: LoginController - Player created: " + username);

        // Use the LoggedInController from AppBuilder if available (it has the correct PreferencesView)
        // Otherwise create a new PreferencesView and LoggedInController
        if (loggedInController == null) {
            System.out.println("DEBUG: LoginController - Creating new PreferencesView and LoggedInController");
            PreferencesViewModel preferencesViewModel = new PreferencesViewModel();
            PreferencesView preferencesView = new PreferencesView(preferencesViewModel);
            
            if (preferencesController != null) {
                System.out.println("DEBUG: LoginController - Setting PreferencesController on PreferencesView");
                preferencesView.setPreferencesController(preferencesController);
            } else {
                System.out.println("ERROR: LoginController - preferencesController is null! PreferencesView won't work.");
            }
            
            // Create LoggedInController with the PreferencesView
            loggedInController = new LoggedInController(preferencesView);
        } else {
            System.out.println("DEBUG: LoginController - Using provided LoggedInController (has correct PreferencesView)");
        }

        LoggedInView loggedInView = new LoggedInView(new LoggedInViewModel(), loggedInController);
        System.out.println("DEBUG: LoginController - Displaying LoggedInView");
        loggedInView.display();
    }

    public void setPreferences(String category, String difficulty, String types, int numQuestions) {
        player.setCategory(category);
        player.setDifficulty(difficulty);
        player.setType(types);
        player.setNumQuestions(numQuestions);
    }
}
