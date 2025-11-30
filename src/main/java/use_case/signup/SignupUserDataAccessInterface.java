package use_case.signup;

import java.io.IOException;

public interface SignupUserDataAccessInterface {

  public void signup(String username, String password) throws IOException;
}
