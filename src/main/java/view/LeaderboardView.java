package view;

import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.leaderboard.LeaderboardViewModel;
import entity.ScoreEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class LeaderboardView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "leaderboard";
    private final LeaderboardViewModel leaderboardViewModel;
    private LeaderboardController leaderboardController;

    private final JLabel titleLabel = new JLabel("LEADERBOARD");
    private final JTextArea scoresArea = new JTextArea(10, 25);
    private final JButton closeButton = new JButton("Close");

    public LeaderboardView(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModel = leaderboardViewModel;
        this.leaderboardViewModel.addPropertyChangeListener(this);

        setupUI();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // title
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        // scores
        scoresArea.setEditable(false);
        scoresArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(scoresArea));

        // close
        closeButton.addActionListener(this);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(closeButton);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(closeButton)) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateDisplay(leaderboardViewModel.getHighScores());
    }

    private void updateDisplay(List<ScoreEntry> highScores) {
        StringBuilder text = new StringBuilder();
        text.append("Rank     Player     Score\n");
        text.append("-------------------------\n");

        if (highScores.isEmpty()) {
            text.append("  No scores yet\n");
        } else {
            for (int i = 0; i < highScores.size(); i++) {
                ScoreEntry entry = highScores.get(i);
                text.append(String.format("%2d.   %-10s %8d\n",
                        i + 1, entry.getUsername(), entry.getScore()));
            }
        }

        scoresArea.setText(text.toString());
    }

    public String getViewName() {
        return viewName;
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public void display() {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if (leaderboardController != null) {
            leaderboardController.showLeaderboard();
        }
    }

}
