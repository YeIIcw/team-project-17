package interface_adapter.HomeScreen;

import interface_adapter.Login.LoginViewModel;
import interface_adapter.Signup.SignUpViewModel;
import view.HomeScreenView;
import view.LoginView;
import view.SignupView;

public class HomeScreenController {

    public void goToLogin() {
        HomeScreenView homeScreenView = new HomeScreenView(new HomeScreenViewModel());

        LoginView loginView = new LoginView(new LoginViewModel());
        loginView.display();
    }

    public void goToCreateAccount() {
        HomeScreenView homeScreenView = new HomeScreenView(new HomeScreenViewModel());

        SignupView signupView = new SignupView(new SignUpViewModel());
        signupView.display();
    }
}
