package app;

import data_access.Gateway.triviaapi.ApiQuestionFetcher;
import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import interface_adapter.AccountCreated.AccountCreatedViewModel;
import interface_adapter.HomeScreen.HomeScreenViewModel;
import interface_adapter.Loggedin.LoggedInController;
import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Preferences.PreferencesController;
import interface_adapter.Preferences.PreferencesPresenter;
import interface_adapter.Preferences.PreferencesViewModel;
import interface_adapter.Signup.SignUpViewModel;
import use_case.preferences.PreferencesInputBoundary;
import use_case.preferences.PreferencesInteractor;
import use_case.preferences.PreferencesOutputBoundary;
import view.*;

import java.util.HashMap;
import java.util.Map;

public class AppBuilder {

    // ====== Views & ViewModels ======

    private HomeScreenView homeScreenView;
    private HomeScreenViewModel homeScreenViewModel;

    private SignupView signupView;
    private SignUpViewModel signUpViewModel;

    private LoginView loginView;
    private LoginViewModel loginViewModel;

    private AccountCreatedView accountCreatedView;
    private AccountCreatedViewModel accountCreatedViewModel;

    private LoggedInView loggedInView;
    private LoggedInViewModel loggedInViewModel;

    private PreferencesView preferencesView;
    private PreferencesViewModel preferencesViewModel;

    // ====== Core game state & controllers ======

    private GameState gameState;
    private LoggedInController loggedInController;

    // ---------- Home Screen ----------

    public AppBuilder addHomeScreenView() {
        homeScreenViewModel = new HomeScreenViewModel();
        homeScreenView = new HomeScreenView(homeScreenViewModel);
        return this;
    }

    public AppBuilder addHomeScreenUseCase() {
        return this;
    }

    // ---------- Signup ----------

    public AppBuilder addSignupView() {
        signUpViewModel = new SignUpViewModel();
        signupView = new SignupView(signUpViewModel);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        // hook up signup use case if needed
        return this;
    }

    // ---------- Login ----------

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        // hook up login use case if needed
        return this;
    }

    // ---------- Account Created ----------

    public AppBuilder addAccountCreatedView() {
        accountCreatedViewModel = new AccountCreatedViewModel();
        accountCreatedView = new AccountCreatedView(accountCreatedViewModel);
        return this;
    }

    public AppBuilder addAccountCreatedUseCase() {
        // hook up account created use case if needed
        return this;
    }

    // ---------- Game State ----------

    public AppBuilder addGameState() {
        this.gameState = new GameState();
        return this;
    }

    // ---------- Preferences (Quiz setup) ----------

    public AppBuilder addPreferencesView() {
        preferencesViewModel = new PreferencesViewModel();
        preferencesView = new PreferencesView(preferencesViewModel);
        return this;
    }

    // This is the real preferences use case wiring
    public AppBuilder addPreferencesUseCase() {
        QuestionFetcher questionFetcher = new ApiQuestionFetcher();

        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("General Knowledge", 9);
        categoryMap.put("Entertainment: Books", 10);
        categoryMap.put("Entertainment: Film", 11);
        categoryMap.put("Entertainment: Music", 12);
        categoryMap.put("Entertainment: Musicals & Theatres", 13);
        categoryMap.put("Entertainment: Television", 14);
        categoryMap.put("Entertainment: Video Games", 15);
        categoryMap.put("Entertainment: Board Games", 16);
        categoryMap.put("Science & Nature", 17);
        categoryMap.put("Science: Computers", 18);
        categoryMap.put("Science: Mathematics", 19);
        categoryMap.put("Mythology", 20);
        categoryMap.put("Sports", 21);
        categoryMap.put("Geography", 22);
        categoryMap.put("History", 23);
        categoryMap.put("Politics", 24);
        categoryMap.put("Art", 25);
        categoryMap.put("Celebrities", 26);
        categoryMap.put("Animals", 27);
        categoryMap.put("Vehicles", 28);
        categoryMap.put("Entertainment: Comics", 29);
        categoryMap.put("Science: Gadgets", 30);
        categoryMap.put("Entertainment: Japanese Anime & Manga", 31);
        categoryMap.put("Entertainment: Cartoon & Animations", 32);

        PreferencesOutputBoundary presenter =
                new PreferencesPresenter(preferencesViewModel);

        PreferencesInputBoundary interactor =
                new PreferencesInteractor(questionFetcher, gameState, presenter, categoryMap);

        PreferencesController preferencesController =
                new PreferencesController(interactor);

        preferencesView.setPreferencesController(preferencesController);

        return this;
    }

    // ---------- Logged In ----------

    // Creates the controller, using the already-wired preferencesView
    public AppBuilder addLoggedInUseCase() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInController = new LoggedInController(preferencesView);
        return this;
    }

    // Creates the LoggedInView using the ViewModel + Controller
    public AppBuilder addLoggedInView() {
        loggedInView = new LoggedInView(loggedInViewModel, loggedInController);
        return this;
    }

    public LoggedInController getLoggedInController() {
        return loggedInController;
    }

    // ---------- Final build ----------

    public void build() {
        // Start the app at the home screen
        homeScreenView.display();
    }
}
