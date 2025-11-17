package use_case.signup;

import java.io.IOException;

public interface SignupInputBoundary {

    public void execute(String username, String password) throws IOException;
}
