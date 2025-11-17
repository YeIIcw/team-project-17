package app;

import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.HomeScreen.HomeScreenViewModel;
import interface_adapter.Signup.SignUpViewModel;
import interface_adapter.AccountCreated.AccountCreatedViewModel;
import interface_adapter.Preferences.PreferencesViewModel;

import view.*;

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
        loginView = new LoginView(loginViewModel);
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

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        return this;
    }
    public AppBuilder addLoggedInUseCase() {
        return this;
    }

    public AppBuilder addPreferencesView() {
        preferencesViewModel = new PreferencesViewModel();
        preferencesView = new PreferencesView(preferencesViewModel);
        return this;
    }

    public AppBuilder addPreferencesUseCase() {
        return this;
    }

    public void build() {
        homeScreenView.display();
    }

}
