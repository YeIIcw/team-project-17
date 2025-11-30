package view;

import entity.GameState;
import interface_adapter.Login.LoginController;
import interface_adapter.Login.LoginViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginView {

  private LoginViewModel loginViewModel;
  private interface_adapter.Preferences.PreferencesController preferencesController;
  private interface_adapter.Loggedin.LoggedInController loggedInController;
  private GameState gameState;

  private final JFrame frame = new JFrame();
  private final JLabel usernameLabel = new JLabel("Username:");
  private final JLabel passwordLabel = new JLabel("Password:");
  private final JTextField usernameText = new JTextField(10);
  private final JPasswordField passwordText = new JPasswordField(10);
  private final JButton loginButton = new JButton("Login");
  private final JButton backButton = new JButton("Back");

  public LoginView(LoginViewModel loginViewModel, GameState gameState) {
    this.loginViewModel = loginViewModel;
    this.gameState = gameState;

    JPanel usernamePanel = new JPanel();
    usernamePanel.add(usernameLabel);
    usernamePanel.add(usernameText);

    JPanel passwordPanel = new JPanel();
    passwordPanel.add(passwordLabel);
    passwordPanel.add(passwordText);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(usernamePanel);
    mainPanel.add(passwordPanel);
    mainPanel.add(loginButton);
    mainPanel.add(backButton);

    frame.add(mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.pack();

    JFrame newFrame = new JFrame("Incorrect username or password!");

    loginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(loginButton)) {
          System.out.println("DEBUG: LoginView - Login button clicked");
          System.out.println(
              "DEBUG: LoginView - preferencesController is " + (preferencesController != null ? "not null" : "null"));

          LoginController loginController = new LoginController();

          if (gameState != null) {
            loginController.setGameState(gameState);
          }

          // Set PreferencesController if available
          if (preferencesController != null) {
            System.out.println("DEBUG: LoginView - Setting PreferencesController on LoginController");
            loginController.setPreferencesController(preferencesController);
          } else {
            System.out.println("ERROR: LoginView - preferencesController is null!");
          }

          // Set LoggedInController if available (this has the correct PreferencesView)
          if (loggedInController != null) {
            System.out.println("DEBUG: LoginView - Setting LoggedInController on LoginController");
            loginController.setLoggedInController(loggedInController);
          } else {
            System.out.println("WARNING: LoginView - loggedInController is null, will create new one");
          }

          try {
            // JFrame newFrame = new JFrame("Incorrect username or password!");
            if (!checkLogin(usernameText.getText(), passwordText.getText())) {
              JPanel incorrect = new JPanel();
              incorrect.setLayout(new BoxLayout(incorrect, BoxLayout.Y_AXIS));
              JLabel incorrectLabel = new JLabel("Incorrect username or password");
              incorrectLabel.setForeground(Color.RED);
              incorrect.add(incorrectLabel);
              mainPanel.add(incorrect);

              newFrame.add(mainPanel);

              newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              newFrame.setLocationRelativeTo(null);
              newFrame.pack();
              newFrame.setVisible(true);
            } else {
              loginController.execute(usernameText.getText(), passwordText.getText());
              frame.dispose();
              newFrame.dispose();
            }

          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    });

    backButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(backButton)) {

          LoginController loginController = new LoginController();
          try {
            loginController.goBack();
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
          frame.dispose();
          newFrame.dispose();
        }
      }
    });
  }

  public void setPreferencesController(interface_adapter.Preferences.PreferencesController controller) {
    System.out.println("DEBUG: LoginView.setPreferencesController() called with controller: "
        + (controller != null ? "not null" : "null"));
    this.preferencesController = controller;
    System.out.println("DEBUG: LoginView - preferencesController field is now: "
        + (this.preferencesController != null ? "not null" : "null"));
  }

  public void setLoggedInController(interface_adapter.Loggedin.LoggedInController controller) {
    this.loggedInController = controller;
  }

  public void display() {
    frame.setVisible(true);
  }

  public boolean checkLogin(String username, String password) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(new File("src/main/java/scratch.txt")));
    String line = br.readLine();

    while (line != null) {
      String[] fields = line.split(", ");
      if (fields[0].equals(username) && fields[1].equals(password)) {
        return true;
      }
      line = br.readLine();
    }

    return false;
  }
}
