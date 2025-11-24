package view;

import app.AppBuilder;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class GameOverView {

    private final JFrame frame;
    private final JLabel titleLabel;
    private final JLabel scoreLabel;
    private final JLabel enemiesLabel;
    private final JButton restartButton;
    private final JButton leaderboardButton;

    private final AppBuilder appBuilder;
    private final int score;
    private final int enemiesDefeated;

    public GameOverView(AppBuilder appBuilder, int score, int enemiesDefeated) {
        this.appBuilder = appBuilder;
        this.score = score;
        this.enemiesDefeated = enemiesDefeated;

        frame = new JFrame("Game Over");
        frame.setSize(400, 230);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        titleLabel = new JLabel("GAME OVER", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        scoreLabel = new JLabel("Score: " + score, JLabel.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        enemiesLabel = new JLabel("Enemies defeated: " + enemiesDefeated, JLabel.CENTER);
        enemiesLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        restartButton = new JButton("Restart Game");
        leaderboardButton = new JButton("See Leaderboard");

        restartButton.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
            appBuilder.resetAndRestart();
        });

        leaderboardButton.addActionListener(e -> {
        });

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.add(titleLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(enemiesLabel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(restartButton);
        bottomPanel.add(leaderboardButton);

        frame.setLayout(new BorderLayout());
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void display() {
        frame.setVisible(true);
    }
}
