package interface_adapter.Login;

import entity.Player;
import interface_adapter.Loggedin.LoggedInViewModel;
import use_case.login.LoginInteractor;
import view.LoggedInView;

import java.io.IOException;

public class LoginController {

    private String username;
    private String password;
    private Player player;

    public void execute(String username, String password) throws IOException {
        this.username = username;
        this.password = password;

        LoginInteractor loginInteractor = new LoginInteractor();
        loginInteractor.execute(username, password);

        this.player = loginInteractor.getPlayer();

        LoggedInView loggedInView = new LoggedInView(new LoggedInViewModel());
        loggedInView.display();
    }

    public void setPreferences(String category, String difficulty, String types, int numQuestions) {
        player.setCategory(category);
        player.setDifficulty(difficulty);
        player.setType(types);
        player.setNumQuestions(numQuestions);
    }
}
