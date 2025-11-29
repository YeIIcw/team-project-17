package interface_adapter.Signup;

import interface_adapter.AccountCreated.AccountCreatedViewModel;
import use_case.signup.SignupInteractor;
import view.AccountCreatedView;
import view.SignupView;

import java.io.IOException;

public class SignupController {

  private String username;
  private String password;

  public void execute(String username, String password) throws IOException {
    this.username = username;
    this.password = password;

    SignupInteractor signupInteractor = new SignupInteractor();
    signupInteractor.execute(username, password);

    SignupView signupView = new SignupView(new SignUpViewModel());

    AccountCreatedView accountCreatedView = new AccountCreatedView(new AccountCreatedViewModel());
    accountCreatedView.display();
  }
}
