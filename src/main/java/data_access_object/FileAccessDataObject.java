package data_access_object;

import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.*;

public class FileAccessDataObject implements LoginUserDataAccessInterface,
        SignupUserDataAccessInterface {

    public void login(String username, String password) throws IOException {
        FileWriter fw = new FileWriter("src/main/java/scratch.txt", true);

        fw.write(username + ", " + password + ", 0\n");
        fw.close();
    }

    public void signup(String username, String password) throws IOException {
        FileWriter fw = new FileWriter("src/main/java/scratch.txt", true);

        fw.write(username + ", " + password + ", 0\n");
        fw.close();
    }

//    public void signup2(String username, String password) {
//
//    }

}
