package app;

import data_access.Gateway.triviaapi.ApiQuestionFetcher;
import data_access.Gateway.triviaapi.QuestionFetcher;

import data_access_object.InMemoryLeaderboardDataAccessObject;

import entity.GameState;
import entity.ScoreEntry;

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

import interface_adapter.Combat.CombatController;
import interface_adapter.Combat.CombatPresenter;
import interface_adapter.Combat.CombatViewModel;

import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardPresenter;
import interface_adapter.leaderboard.LeaderboardViewModel;

import use_case.gameplay.GameplayInputBoundary;
import use_case.gameplay.GameplayInteractor;
import use_case.gameplay.GameplayOutputBoundary;

import use_case.leaderboard.LeaderboardDataAccessInterface;
import use_case.leaderboard.LeaderboardInputBoundary;
import use_case.leaderboard.LeaderboardInteractor;
import use_case.leaderboard.LeaderboardOutputBoundary;

import use_case.preferences.PreferencesInputBoundary;
import use_case.preferences.PreferencesInteractor;

import use_case.combat.CombatInteractor;

import view.*;

import java.util.HashMap;
import java.util.Map;

public class AppBuilder {

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
    private GameplayPresenter gameplayPresenter;
    private GameplayInteractor gameplayInteractor;

    private CombatView combatView;
    private CombatViewModel combatViewModel;
    private CombatController combatController;
    private CombatInteractor combatInteractor;
    private CombatPresenter combatPresenter;

    private LeaderboardController leaderboardController;
    private LeaderboardView leaderboardView;

    private GameState gameState;
    private LoggedInController loggedInController;

    public void resetAndRestart() {
        System.out.println("DEBUG: AppBuilder - FULL RESET");

        this.gameState = new GameState();

        rebuildCombatStack();

        preferencesView.getFrame().setVisible(true);
    }

    private void rebuildCombatStack() {
        System.out.println("DEBUG: AppBuilder - rebuildCombatStack()");

        combatViewModel  = new CombatViewModel();
        combatPresenter  = new CombatPresenter(combatViewModel);
        combatInteractor = new CombatInteractor(combatPresenter, gameState);
        combatController = new CombatController(combatInteractor);
        combatView       = new CombatView(combatController, combatViewModel);

        // Combat -> Question köprüsü
        if (gameplayView != null) {
            gameplayView.setCombatController(combatController);

            combatView.setOnQuestionRequested(() -> {
                String diff = combatViewModel.getQuestionDifficulty();
                if (diff == null) diff = "Question";
                gameplayView.askCombatQuestion(diff);
            });
        }

        combatPresenter.setUiUpdateCallback(() -> combatView.refreshFromViewModel());

        // Game Over screen
        combatPresenter.setGameOverCallback(() -> {
            combatView.setVisibleFalse();

            int enemiesDefeated = gameState.getEnemyIndex();
            int finalScore = gameState.getScore();

            if (leaderboardController != null) {
                String usernameToUse = gameState.getCurrentUsername();
                System.out.println("DEBUG: Using username from GameState: " + usernameToUse);

                leaderboardController.execute(usernameToUse, finalScore);
                System.out.println("DEBUG: Score saved for " + usernameToUse + ": " + finalScore);
                leaderboardController.showLeaderboard();
            } else {
                System.out.println("DEBUG: LeaderboardController is null");
            }

//                String testUsername = "TestPlayer";
//                leaderboardController.execute(testUsername, finalScore);
//                System.out.println("DEBUG: Score saved for " + testUsername + ": " + finalScore);
//
//                System.out.println("DEBUG: Showing leaderboard");
//                leaderboardController.showLeaderboard();
//
//            } else {
//                System.out.println("DEBUG: LeaderboardController is null");
//            }

            GameOverView gov = new GameOverView(this, gameState.getScore(), gameState.getEnemiesDefeated());
            gov.setLeaderboardView(leaderboardView);
            gov.display();
        });

    }

    public AppBuilder addHomeScreenView() {
        homeScreenViewModel = new HomeScreenViewModel();
        homeScreenView = new HomeScreenView(homeScreenViewModel);
        return this;
    }

    public AppBuilder addHomeScreenUseCase() {
        return this;
    }

    public AppBuilder addSignupView() {
        signUpViewModel = new SignUpViewModel();
        signupView = new SignupView(signUpViewModel);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, gameState);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        return this;
    }

    public AppBuilder addAccountCreatedView() {
        accountCreatedViewModel = new AccountCreatedViewModel();
        accountCreatedView = new AccountCreatedView(accountCreatedViewModel);
        return this;
    }

    public AppBuilder addAccountCreatedUseCase() {
        return this;
    }

    public AppBuilder addGameState() {
        this.gameState = new GameState();
        return this;
    }

    public AppBuilder addCombatUseCase() {
        rebuildCombatStack();
        return this;
    }

    public AppBuilder addPreferencesView() {
        preferencesViewModel = new PreferencesViewModel();
        preferencesView = new PreferencesView(preferencesViewModel);
        return this;
    }

    public AppBuilder addPreferencesUseCase() {
        QuestionFetcher questionFetcher = new ApiQuestionFetcher();
        System.out.println("DEBUG: AppBuilder - Using FakeQuestionFetcher");

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

        presenter.setOnSuccessCallback(() -> {
            System.out.println("DEBUG: AppBuilder - Preferences success → COMBAT");
            preferencesView.getFrame().dispose();
            combatController.startBattle();
            combatView.display();
        });

        if (loginView != null) {
            loginView.setPreferencesController(this.preferencesController);
            System.out.println("DEBUG: AppBuilder - PreferencesController set on LoginView");
        }

        return this;
    }

    public AppBuilder addLoggedInUseCase() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInController = new LoggedInController(preferencesView);

        if (loginView != null)
            loginView.setLoggedInController(loggedInController);

        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInView = new LoggedInView(loggedInViewModel, loggedInController);

        if (homeScreenView != null && loginView != null && signupView != null) {
            var homeCtrl = new interface_adapter.HomeScreen.HomeScreenController(loginView, signupView, gameState);
            homeScreenView.setHomeScreenController(homeCtrl);
        }

        return this;
    }

    public AppBuilder addGameplayView() {
        gameplayViewModel = new GameplayViewModel();
        gameplayViewModel.setHasMoreQuestions(true);
        return this;
    }

    public AppBuilder addGameplayUseCase() {
        gameplayPresenter = new GameplayPresenter(gameplayViewModel);
        gameplayInteractor = new GameplayInteractor(gameState, gameplayPresenter, this);
        GameplayInputBoundary interactor = gameplayInteractor;
        GameplayController controller = new GameplayController(interactor);

        gameplayView = new GameplayView(gameplayViewModel, controller);
        return this;
    }

    public AppBuilder addLeaderboardUseCase() {
        System.out.println("DEBUG: AppBuilder - leaderboard system");

        LeaderboardDataAccessInterface dataAccess = new InMemoryLeaderboardDataAccessObject();
        LeaderboardViewModel viewModel = new LeaderboardViewModel();
        LeaderboardOutputBoundary presenter = new LeaderboardPresenter(viewModel);
        LeaderboardInputBoundary interactor = new LeaderboardInteractor(dataAccess, presenter);
        LeaderboardController controller = new LeaderboardController(interactor);

        LeaderboardView leaderboardView = new LeaderboardView(viewModel);
        leaderboardView.setLeaderboardController(controller);

        this.leaderboardController = controller;
        this.leaderboardView = leaderboardView;
        leaderboardView.display();

        return this;
    }

    public void build() {
        homeScreenView.display();
    }
}
