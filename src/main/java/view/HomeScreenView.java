package view;

import interface_adapter.HomeScreen.HomeScreenController;
import interface_adapter.HomeScreen.HomeScreenViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeScreenView {

  private HomeScreenViewModel homescreenViewModel;
  private interface_adapter.HomeScreen.HomeScreenController homeScreenController;

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
        if (e.getSource().equals(loginButton)) {
          if (homeScreenController != null) {
            homeScreenController.goToLogin();
          } else {
            System.out.println("ERROR: HomeScreenView - homeScreenController is null!");
            // Fallback: create new controller
            HomeScreenController newController = new HomeScreenController();
            newController.goToLogin();
          }
          frame.dispose();
        }
      }
    });

    createAccount.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(createAccount)) {
          if (homeScreenController != null) {
            homeScreenController.goToCreateAccount();
          } else {
            System.out.println("ERROR: HomeScreenView - homeScreenController is null!");
            // Fallback: create new controller
            HomeScreenController newController = new HomeScreenController();
            newController.goToCreateAccount();
          }
          frame.dispose();
        }
      }
    });
  }

  public void setHomeScreenController(interface_adapter.HomeScreen.HomeScreenController controller) {
    this.homeScreenController = controller;
  }

  public void display() {
    frame.setVisible(true);
  }
}
