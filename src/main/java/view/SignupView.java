package view;

import interface_adapter.Signup.SignUpViewModel;
import interface_adapter.Signup.SignupController;

import javax.swing.*;
import java.io.IOException;

public class SignupView {

    private SignUpViewModel signUpViewModel;

    private final JFrame frame = new JFrame();
    private final JLabel usernameLabel = new JLabel("Username:");
    private final JLabel passwordLabel = new JLabel("Password:");
    private final JTextField usernameText = new JTextField(10);
    private final JPasswordField passwordText = new JPasswordField(10);
    private final JButton createButton = new JButton("Create");

    public SignupView(SignUpViewModel signUpViewModel) {
        this.signUpViewModel = signUpViewModel;

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
        mainPanel.add(createButton);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();

        createButton.addActionListener(e -> {
            if (e.getSource().equals(createButton)) {
                SignupController signupController = new SignupController();

                try {

                    if (usernameText.getText().equals("") || passwordText.getText().equals("")) {
                        frame.add(new JLabel("Please fill all the fields!"));
                        frame.pack();
                    }
                    else {
                        signupController.execute(usernameText.getText(), passwordText.getText());
                        frame.dispose();
                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void display() {
        frame.setVisible(true);
    }

}
