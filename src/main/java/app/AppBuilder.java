package app;

import data_access.Gateway.triviaapi.ApiQuestionFetcher;
import data_access.Gateway.triviaapi.FakeQuestionFetcher;
import data_access.Gateway.triviaapi.QuestionFetcher;
import entity.GameState;
import interface_adapter.AccountCreated.AccountCreatedViewModel;
import interface_adapter.HomeScreen.HomeScreenViewModel;
import interface_adapter.Loggedin.LoggedInController;
import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Gameplay.GameplayController;
import interface_adapter.Gameplay.GameplayPresenter;
import interface_adapter.Gameplay.GameplayViewModel;
import interface_adapter.Preferences.PreferencesController;
import interface_adapter.Preferences.PreferencesPresenter;
import interface_adapter.Preferences.PreferencesViewModel;
import interface_adapter.Signup.SignUpViewModel;
import use_case.gameplay.GameplayInputBoundary;
import use_case.gameplay.GameplayInteractor;
import use_case.gameplay.GameplayOutputBoundary;
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
    private PreferencesController preferencesController;

    private GameplayView gameplayView;
    private GameplayViewModel gameplayViewModel;

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
        // Wire HomeScreenController with views after they're created
        // This happens after addLoginView() and addSignupView()
        return this;
    }

    public HomeScreenView getHomeScreenView() {
        return homeScreenView;
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
        // Note: LoginController is created in LoginView, but we need to wire it
        // with LoggedInController after both are created
        // This is done in addLoggedInUseCase() after LoggedInController is created
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
        // Use FakeQuestionFetcher for testing (no API calls)
        // Switch to ApiQuestionFetcher() when API is working
        QuestionFetcher questionFetcher = new FakeQuestionFetcher();
        System.out.println("DEBUG: AppBuilder - Using FakeQuestionFetcher for testing");

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

        PreferencesPresenter presenter =
                new PreferencesPresenter(preferencesViewModel);

        PreferencesInputBoundary interactor =
                new PreferencesInteractor(questionFetcher, gameState, presenter, categoryMap);

        this.preferencesController =
                new PreferencesController(interactor);

        preferencesView.setPreferencesController(this.preferencesController);
        System.out.println("DEBUG: AppBuilder - PreferencesController set on PreferencesView");

        // Also set PreferencesController on LoginView so it can pass it to LoginController
        if (loginView != null) {
            loginView.setPreferencesController(this.preferencesController);
            System.out.println("DEBUG: AppBuilder - PreferencesController set on LoginView");
        }

        // Set callback to open gameplay view when questions are successfully fetched
        presenter.setOnSuccessCallback(() -> {
            System.out.println("DEBUG: AppBuilder - Success callback triggered");
            System.out.println("DEBUG: AppBuilder - Disposing PreferencesView frame");
            preferencesView.getFrame().dispose();
            
            if (gameplayView != null) {
                System.out.println("DEBUG: AppBuilder - GameplayView exists, displaying it");
                gameplayView.display();
                System.out.println("DEBUG: AppBuilder - GameplayView displayed");
            } else {
                System.out.println("ERROR: AppBuilder - gameplayView is null! Cannot display gameplay.");
            }
        });

        return this;
    }

    public PreferencesView getPreferencesView() {
        return preferencesView;
    }

    public PreferencesController getPreferencesController() {
        return preferencesController;
    }

    // ---------- Logged In ----------

    // Creates the controller, using the already-wired preferencesView
    public AppBuilder addLoggedInUseCase() {
        System.out.println("DEBUG: AppBuilder - addLoggedInUseCase() called");
        loggedInViewModel = new LoggedInViewModel();
        loggedInController = new LoggedInController(preferencesView);
        System.out.println("DEBUG: AppBuilder - LoggedInController created with PreferencesView");
        
        // Also set LoggedInController on LoginView so LoginController can use it
        if (loginView != null) {
            loginView.setLoggedInController(loggedInController);
            System.out.println("DEBUG: AppBuilder - LoggedInController set on LoginView");
        }
        
        return this;
    }

    public LoggedInController getLoggedInController() {
        return loggedInController;
    }

    // Creates the LoggedInView using the ViewModel + Controller
    public AppBuilder addLoggedInView() {
        loggedInView = new LoggedInView(loggedInViewModel, loggedInController);
        
        // Wire HomeScreenController with views now that all views are created
        if (homeScreenView != null && loginView != null && signupView != null) {
            interface_adapter.HomeScreen.HomeScreenController homeScreenController = 
                new interface_adapter.HomeScreen.HomeScreenController(loginView, signupView);
            homeScreenView.setHomeScreenController(homeScreenController);
            System.out.println("DEBUG: AppBuilder - HomeScreenController wired with LoginView and SignupView");
        }
        
        return this;
    }

    // ---------- Gameplay ----------

    public AppBuilder addGameplayView() {
        System.out.println("DEBUG: AppBuilder - addGameplayView() called");
        gameplayViewModel = new GameplayViewModel();
        System.out.println("DEBUG: AppBuilder - GameplayViewModel created");
        return this;
    }

    public AppBuilder addGameplayUseCase() {
        System.out.println("DEBUG: AppBuilder - addGameplayUseCase() called");
        
        if (gameplayViewModel == null) {
            throw new IllegalStateException("GameplayViewModel must be created before use case");
        }

        GameplayOutputBoundary presenter = new GameplayPresenter(gameplayViewModel);
        System.out.println("DEBUG: AppBuilder - GameplayPresenter created");
        
        GameplayInputBoundary interactor = new GameplayInteractor(gameState, presenter);
        System.out.println("DEBUG: AppBuilder - GameplayInteractor created");
        
        GameplayController controller = new GameplayController(interactor);
        System.out.println("DEBUG: AppBuilder - GameplayController created");
        
        gameplayView = new GameplayView(gameplayViewModel, controller);
        System.out.println("DEBUG: AppBuilder - GameplayView created");
        // First question is loaded automatically in GameplayView constructor

        return this;
    }

    // ---------- Final build ----------

    public void build() {
        // Start the app at the home screen
        homeScreenView.display();
    }
}
