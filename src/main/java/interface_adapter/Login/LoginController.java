package interface_adapter.Login;

import entity.Player;
import interface_adapter.Loggedin.LoggedInController;
import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Preferences.PreferencesController;
import use_case.login.LoginInteractor;
import view.LoggedInView;

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

        // Verify we have the necessary controllers
        if (loggedInController == null) {
            System.out.println("ERROR: LoginController - loggedInController is null!");
            throw new IllegalStateException("LoggedInController must be set before login");
        }

        if (preferencesController == null) {
            System.out.println("ERROR: LoginController - preferencesController is null!");
            throw new IllegalStateException("PreferencesController must be set before login");
        }

        System.out.println("DEBUG: LoginController - Using LoggedInController from AppBuilder");

        // Create and display LoggedInView using the properly wired controller
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