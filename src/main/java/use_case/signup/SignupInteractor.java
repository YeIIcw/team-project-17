package use_case.signup;

import data_access_object.FileAccessDataObject;

import java.io.IOException;

public class SignupInteractor implements SignupInputBoundary{

    public void execute(String username, String password) throws IOException {

        FileAccessDataObject fileAccessDataObject = new FileAccessDataObject();
        fileAccessDataObject.signup(username, password);
    }
}
