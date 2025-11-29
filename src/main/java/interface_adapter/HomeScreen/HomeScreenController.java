package interface_adapter.HomeScreen;

import entity.GameState;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Signup.SignUpViewModel;
import view.LoginView;
import view.SignupView;

public class HomeScreenController {

  private LoginView loginView;
  private SignupView signupView;
  private GameState gameState;

  public HomeScreenController() {
    // Default constructor
  }

  public HomeScreenController(LoginView loginView, SignupView signupView, GameState gameState) {
    this.loginView = loginView;
    this.signupView = signupView;
    this.gameState = gameState;
  }

  public void setLoginView(LoginView loginView) {
    this.loginView = loginView;
  }

  public void setSignupView(SignupView signupView) {
    this.signupView = signupView;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public void goToLogin() {
    if (loginView != null) {
      System.out.println("DEBUG: HomeScreenController - Using LoginView from AppBuilder");
      loginView.display();
    } else {
      System.out.println("WARNING: HomeScreenController - loginView is null, creating new one");
      LoginView newLoginView = new LoginView(new LoginViewModel(), gameState);
      newLoginView.display();
    }
  }

  public void goToCreateAccount() {
    if (signupView != null) {
      System.out.println("DEBUG: HomeScreenController - Using SignupView from AppBuilder");
      signupView.display();
    } else {
      System.out.println("WARNING: HomeScreenController - signupView is null, creating new one");
      SignupView newSignupView = new SignupView(new SignUpViewModel());
      newSignupView.display();
    }
  }
}
