package view;

import interface_adapter.Loggedin.LoggedInController;
import interface_adapter.Loggedin.LoggedInViewModel;
import interface_adapter.Logout.LogoutController;

import javax.swing.*;

public class LoggedInView {

    private LoggedInViewModel loggedInViewModel;
    private LoggedInController loggedInController;

    private final JFrame frame = new JFrame();

    private final JLabel header = new JLabel("You are now logged in.");
    private final JButton playButton = new JButton("Play");
    private final JButton statsButton = new JButton("Stats");
    private final JButton logoutButton = new JButton("Logout");

    public LoggedInView(LoggedInViewModel loggedInViewModel, LoggedInController loggedInController) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInController = loggedInController;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(header);
        panel.add(playButton);
        panel.add(statsButton);
        panel.add(logoutButton);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();

        //play button clicked
        playButton.addActionListener(e -> {

            if (e.getSource().equals(playButton)) {
                loggedInController.goToPreferences();
                frame.dispose();
            }
        });

        //stats button clicked
        statsButton.addActionListener(e -> {

            if (e.getSource().equals(statsButton)) {
                loggedInController.goToStats();
                frame.dispose();
            }
        });

        //logout button clicked
        logoutButton.addActionListener(e -> {

            if (e.getSource().equals(logoutButton)) {
                LogoutController logoutController = new LogoutController();
                logoutController.execute();
                frame.dispose();
            }
        });
    }

    public void display() {
        frame.setVisible(true);
    }

}
