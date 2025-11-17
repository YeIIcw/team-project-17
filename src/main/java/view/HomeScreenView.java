package view;

import interface_adapter.HomeScreen.HomeScreenController;
import interface_adapter.HomeScreen.HomeScreenViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeScreenView {

    private HomeScreenViewModel homescreenViewModel;

    private final JFrame frame = new JFrame();

    private final JButton loginButton = new JButton("Login");
    private final JButton createAccount = new JButton("Create Account");

    private final JPanel homePanel = new JPanel();

    public HomeScreenView(HomeScreenViewModel homescreenViewModel) {
        this.homescreenViewModel = homescreenViewModel;

        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.add(loginButton);
        homePanel.add(createAccount);
        frame.add(homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeScreenController homeScreenController = new HomeScreenController();

                if (e.getSource().equals(loginButton)) {
                    homeScreenController.goToLogin();
                    frame.dispose();
                }
            }
        });

        createAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomeScreenController homeScreenController = new HomeScreenController();

                if (e.getSource().equals(createAccount)) {
                    homeScreenController.goToCreateAccount();
                    frame.dispose();
                }
            }
        });
    }

    public void display() {
        frame.setVisible(true);
    }
}
