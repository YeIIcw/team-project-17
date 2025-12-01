package use_case.signup;

import interface_adapter.Signup.SignupController;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SignupTests {

    @Test
    public void signup() throws IOException {
        SignupController signupController = new SignupController();
        signupController.execute("testing_username", "testing_password");

        assertTrue(inTextFile("testing_username", "testing_password"));
    }

    @Test
    public void signup2() throws IOException {
        SignupController signupController = new SignupController();
        signupController.execute("testing_username1", "testing_password");
        assertFalse(inTextFile("testing_username1", "testing_password1"));
    }

    public boolean inTextFile(String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/java/scratch.txt"));
        String line = reader.readLine();

        while (line != null) {
            String[] split = line.split(", ");
            if (split[0].equals(username) && split[1].equals(password)) {
                return true;
            }
            line = reader.readLine();
        }

        return false;
    }
}
