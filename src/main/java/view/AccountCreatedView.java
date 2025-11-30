package view;

import interface_adapter.AccountCreated.AccountCreatedController;
import interface_adapter.AccountCreated.AccountCreatedViewModel;

import javax.swing.*;

public class AccountCreatedView {

  private AccountCreatedViewModel accountCreatedViewModel;

  private final JFrame frame = new JFrame();
  private final JLabel header = new JLabel("New Account Created");
  private final JButton backToHomeButton = new JButton("Back to Home");

  public AccountCreatedView(AccountCreatedViewModel accountCreatedViewModel) {
    this.accountCreatedViewModel = accountCreatedViewModel;

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(header);
    mainPanel.add(backToHomeButton);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(mainPanel);
    frame.setLocationRelativeTo(null);
    frame.pack();

    backToHomeButton.addActionListener(e -> {

      if (e.getSource().equals(backToHomeButton)) {
        AccountCreatedController accountCreatedController = new AccountCreatedController();
        accountCreatedController.execute();
        frame.dispose();
      }
    });
  }

  public void display() {
    frame.setVisible(true);
  }
}
