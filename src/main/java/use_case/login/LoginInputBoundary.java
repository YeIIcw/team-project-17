package use_case.login;

import java.io.IOException;

public interface LoginInputBoundary {

  public void execute(String username, String password) throws IOException;
}
