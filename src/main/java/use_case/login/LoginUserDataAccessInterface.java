package use_case.login;

import java.io.IOException;

public interface LoginUserDataAccessInterface {

  public void login(String username, String password) throws IOException;
}
